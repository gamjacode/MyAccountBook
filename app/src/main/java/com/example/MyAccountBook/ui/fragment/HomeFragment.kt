package com.example.myaccountbook.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myaccountbook.database.AccountEntity
import com.example.myaccountbook.ui.AddItemActivity
import com.example.myaccountbook.ui.adapter.AccountAdapter
import com.example.myaccountbook.ui.viewmodel.AccountViewModel
import com.example.myaccountbook.util.CalculateToday
import com.example.myaccountbook.util.AccountString
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import androidx.lifecycle.Observer
import com.example.MyAccountBook.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*
import kotlin.math.absoluteValue

class HomeFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var adapter: AccountAdapter

    private var income: Int = 0
    private var pay: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)

        rootView.home_add_button.setOnClickListener {   //추가하기를 눌렀을때
            val date = home_tv_date.text
            val intent = Intent(rootView.context, AddItemActivity::class.java)
            intent.putExtra(AccountString().EXTRA_ACCOUNT_DATE, date) //??.. 뭘넘기는 거지?..
            startActivity(intent)
        }
        //어뎁터를 설정하면서
        adapter =
            AccountAdapter {    //accountitem을 클릭했을때
                val intent = Intent(rootView.context, AddItemActivity::class.java)
                intent.putExtra(
                    AccountString().EXTRA_ACCOUNT_ID, it.id
                )
                intent.putExtra(AccountString().EXTRA_ACCOUNT_CATEGORY, it.category)
                intent.putExtra(AccountString().EXTRA_ACCOUNT_MONEY, it.money)
                intent.putExtra(AccountString().EXTRA_ACCOUNT_DATE, it.date)
                startActivity(intent)
            }

        var day = 0
        val date = calculateDayMonthYear(day)
        calculateDate(date)
        setHeader(rootView, date)
        calculateIncomeAndPay(rootView, date)

        rootView.home_prev_day.setOnClickListener {
            day -= 1
            val date = calculateDayMonthYear(day)
            calculateDate(date)
            setHeader(rootView, date)
            calculateIncomeAndPay(rootView, date)
        }
        rootView.home_next_day.setOnClickListener {
            day += 1
            val date = calculateDayMonthYear(day)
            calculateDate(date)
            setHeader(rootView, date)
            calculateIncomeAndPay(rootView, date)
        }


        val layoutManager = LinearLayoutManager(rootView.context)
        rootView.home_recycler_view.adapter = adapter
        rootView.home_recycler_view.layoutManager = layoutManager
        rootView.home_recycler_view.setHasFixedSize(true)

        return rootView
    }

    private fun calculateIncomeAndPay(rootView:View, date: List<Int>){
        accountViewModel.getAccount("%${date[2]}년 ${date[1]}월 ${date[0]}일%")
            .observe(this, Observer<List<AccountEntity>>() {
                income = 0
                pay = 0
                it.forEach {account->
                    if(account.category == AccountString().INCOME){
                        income+=account.money
                    }else{
                        pay-=account.money
                    }
                }
                rootView.home_tv_total_income.text = income.toString()
                rootView.home_tv_total_pay.text = pay.toString()
            })
    }

    private fun calculateDayMonthYear(day: Int):List<Int> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, day)
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)
        val curMonth = calendar.get(Calendar.MONTH) + 1
        val curYear = calendar.get(Calendar.YEAR)

        return listOf(curDay, curMonth, curYear)
    }

    private fun calculateDate(date: List<Int>) {
        accountViewModel.getAccount("%${date[2]}년 ${date[1]}월 ${date[0]}일%")
            .observe(this, Observer<List<AccountEntity>>() {
                adapter.setAccounts(it)
            })
    }

    private fun setHeader(rootView:View, date:List<Int>){
        rootView.home_tv_date.text = "${date[2]}년 ${date[1]}월 ${date[0]}일"
    }
}
