package com.example.myaccountbook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.MyAccountBook.R
import com.example.myaccountbook.ui.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_view_pager.adapter = ViewPagerAdapter(supportFragmentManager) //어뎁터와 viewpager를 연결
        main_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            //뷰페이저 내부의 페이지의 변화가 생겼을때 호출되는 부분들을 나타내줌
            override fun onPageScrollStateChanged(state: Int) {}  //포지션(페이지 위치)와는 상관이 없을 때

            override fun onPageScrolled(  //scroll(slide)가 될 때 마다 계속해서 호출되는 부분
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                main_navigation.menu.getItem(position).isChecked
            }
        })

        main_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.calendar -> main_view_pager.currentItem = 0
                R.id.home -> main_view_pager.currentItem = 1
            }
            true
        }
    }
}
