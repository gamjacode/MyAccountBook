package com.example.myaccountbook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProviders
import com.example.MyAccountBook.R
import com.example.myaccountbook.database.AccountEntity
import com.example.myaccountbook.ui.viewmodel.AccountViewModel
import com.example.myaccountbook.util.AccountString
import kotlinx.android.synthetic.main.activity_add_item.*

class AddItemActivity : AppCompatActivity() {

    private lateinit var accountViewModel: AccountViewModel //뷰모델을 써야하니
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        //1) ViewModel 객체를 만들어 viewmodel에 있는 것들을 다 쓸 수 있게 해줌
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)

        //2) 만약 intent가 null이 아니고, EditText와 id값을 지정해준다.
        //MainActivity에서 ADD 버튼을 눌렀을 때에는 신규 추가이므로 인텐트가 없고, RecyclerView item을 눌렀을 때에는
        //편집을 할 때에는 해당하는 정보를 불러오기 위해 인텐트 값을 불러올 것이다.
        if (intent != null
            && intent.hasExtra(AccountString().EXTRA_ACCOUNT_ID)    //해당 key가 전달할 값을 가지고 있다면
            && intent.hasExtra(AccountString().EXTRA_ACCOUNT_CATEGORY)
            && intent.hasExtra(AccountString().EXTRA_ACCOUNT_MONEY)
            && intent.hasExtra(AccountString().EXTRA_ACCOUNT_DATE)
        ) {
            add_item_add_button.text = "수정"                   //등록버튼이 수정버튼으로 바뀜
            add_item_remove_button.visibility = View.VISIBLE    //삭제 버튼이 나타남
            add_item_input_money.setText(
                intent.getIntExtra(
                    AccountString().EXTRA_ACCOUNT_MONEY,
                    0
                ).toString()
            )

            /*
            아이디 값이 null일 경우 Room에서 자동으로 id를 생성해주면서 새로운 account를 DB에 추가한다.
            id값을 Main에서 intent로 받아온 경우, 완료 버튼을 누르면 해당 아이템을 수정하게 된다.
            DAO에서 OnConflictStrategy를 REPLACE로 설정해두었기 때문이다.
            */

            //커서의 위치를 이동시켜주는거 add_item_input_money 문자열의 맨 마지막에 커서를 위치시켜줌
            add_item_input_money.setSelection(add_item_input_money.text.length)


            if (intent.getStringExtra(AccountString().EXTRA_ACCOUNT_CATEGORY) == AccountString().INCOME) {
                add_item_radio_button_income.isChecked = true
            } else {
                add_item_radio_button_pay.isChecked = true
            }
            id = intent.getLongExtra(AccountString().EXTRA_ACCOUNT_ID, -1)
        }

        add_item_add_button.setOnClickListener {
            val account = makeAccount()
            accountViewModel.insertAccount(account)
            finish()
        }

        add_item_remove_button.setOnClickListener {
            val account = makeAccount()
            accountViewModel.deleteAccount(account)
            finish()
        }
    }

    private fun makeAccount(): AccountEntity {
        val categoryId = add_item_radio_group.checkedRadioButtonId  //라디오 버튼에서 뭐가 선택되었는지
        val category = findViewById<RadioButton>(categoryId).text.toString() //수입 or 지출?
        val money = add_item_input_money.text.toString().trim().toInt() //액수 trim()문자열 공백제거
        val date = intent.getStringExtra(AccountString().EXTRA_ACCOUNT_DATE) ?: ""
        return AccountEntity(id, category, money, date)
    }
}
