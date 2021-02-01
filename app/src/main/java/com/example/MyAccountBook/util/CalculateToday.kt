package com.example.myaccountbook.util

import java.text.SimpleDateFormat
import java.util.*

class CalculateToday {
    private val now: Long = System.currentTimeMillis()
    private val date = Date(now)

    private val yearFormat = SimpleDateFormat(CalendarDateFormat().CALENDAR_YEAR_FORMAT, Locale.KOREA)
    private val monthFormat = SimpleDateFormat(CalendarDateFormat().CALENDAR_MONTH_FORAMT, Locale.KOREA)
    private val dayFormat = SimpleDateFormat(CalendarDateFormat().CALENDAR_DAY_FORAMT, Locale.KOREA)

    val year = yearFormat.format(date).toInt()
    val month = monthFormat.format(date).toInt()
    val day = dayFormat.format(date).toInt()
}