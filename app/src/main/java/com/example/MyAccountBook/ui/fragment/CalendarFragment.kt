package com.example.myaccountbook.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.MyAccountBook.R
import com.example.myaccountbook.database.AccountEntity
import com.example.myaccountbook.ui.AddItemActivity
import com.example.myaccountbook.ui.adapter.CalendarAdapter
import com.example.myaccountbook.ui.viewmodel.AccountViewModel
import com.example.myaccountbook.util.AccountString
import com.example.myaccountbook.util.CalculateToday
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.android.synthetic.main.item_calendar.view.*
import java.util.*
import kotlin.math.absoluteValue
//https://link2me.tistory.com/1713 날짜 관련
class CalendarFragment : Fragment() {

    private val dayList = ArrayList<String>()
    private lateinit var accountViewModel: AccountViewModel
    private val money = mutableMapOf<String, Int>()  //money map을 만듬

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView =      //fragment xml을 담아줌
            inflater.inflate(R.layout.fragment_calendar, container, false)

        var month = 0

        calculateMoney(month, rootView)

        rootView.prev_month.setOnClickListener {
            month--
            calculateMoney(month, rootView)
        }

        rootView.next_month.setOnClickListener {
            month++
            calculateMoney(month, rootView)
        }

        return rootView
    }
/*
Calendar.set() - 날짜 설정하기, Calendar.add() - 날짜 더하기, Calendar.roll() - 그 부분만 날짜 더하기
 */
    private fun calculateMonthAndYear(month: Int): List<Int> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, month) //Calendar.add(더할 곳 , 더할 값)
        val curMonth = calendar.get(Calendar.MONTH) + 1 //설정한 month+1을 담아줌(냅두면 현재)
        val curYear = calendar.get(Calendar.YEAR) //현재 year를 담아줌
        return listOf<Int>(curMonth, curYear) //리스트 형식으로 달과 해를 넘겨줌
    }

    private fun calculateMoney(month: Int, rootView: View) {
        val monthAndYear = calculateMonthAndYear(month) //현재 달과 해를 가져왔음
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        accountViewModel.getAccount("%${monthAndYear[1]}년 ${monthAndYear[0]}월%")
        //Dao에 있는 getAccount를 이용해서 날짜를 Entitiy에 넣어줌

            .observe(this, androidx.lifecycle.Observer<List<AccountEntity>> {
                //observer는 onChanged 메소드를 가지고 있다. 즉, 관찰하고 있던 LiveData가 변하면 무엇을 할 것인지 액션을 지정할 수 있다.
                //LiveData를 설정
                money.clear()
                it.forEach { accountEntity ->   //일단 pass
                    val date = accountEntity.date.split("${monthAndYear[1]}년 ${monthAndYear[0]}월 ")
                    if (date.size == 2) {
                        val dateString = date[1]
                        if (money[dateString] == null) {
                            money[dateString] = 0
                        }
                        if (accountEntity.category == AccountString().INCOME) {
                            money[dateString] = ((money[dateString] ?: 0).plus(accountEntity.money))
                        } else {
                            money[dateString] = (money[dateString] ?: 0).minus(accountEntity.money)
                        }

                    }
                }
                setCalendar(month, rootView, money)
            })
    }

    private fun setCalendarDate(month: Int, mCal: Calendar) {   //월,일 설정
        mCal.set(Calendar.MONTH, month - 1) //캘린더에 월을 세팅 0~11월로 인식하므로 -1, 입력된 년월

        for (i in 0 until mCal.getActualMaximum(Calendar.DAY_OF_MONTH)) { //지정한 월의 마지막 날짜까지 반복
            dayList.add("${i + 1}") //1월이면 31까지 2월이면 29까지
        }
    }

    private fun setCalendar(month: Int, rootView: View, moneyDate: MutableMap<String, Int>?) {
        dayList.clear()
        val monthAndYear = calculateMonthAndYear(month)  //리스트형식으로 월과 년을 받음

        rootView.tv_date.text = "${monthAndYear[1]}년 ${monthAndYear[0]}월"  //몇년 몇월인지 위에 표시해줌

        val mCal = Calendar.getInstance()
        mCal.set(monthAndYear[1], monthAndYear[0] - 1, 1) //가져온 month,year로 날짜를 세팅
        val dayNum = mCal.get(Calendar.DAY_OF_WEEK)
        //dayNum = 현재 요일(일요일은 1, 토요일은 7)
        for (i in 1 until dayNum) {
            dayList.add("")   //맨앞에 공란 만들어주기 위해
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1, mCal) //일을 세팅

        val adapter = CalendarAdapter(rootView.context, dayList, moneyDate, "${monthAndYear[1]}년 ${monthAndYear[0]}월")
        rootView.gridview.adapter = adapter
    }
}
