package ru.hse.miem.miemapp.presentation.schedule

import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class CalendarHelper {

    private val systemLanguage = Locale.getDefault().displayLanguage

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd")

    fun getCurrentDate(): String {

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return getApiFormattedDate(year, month, day)
    }

    fun getLastDate(currentDate: String): String {

        val tempCalendar = Calendar.getInstance()

        tempCalendar.time = dateFormat.parse(currentDate)

        tempCalendar.add(Calendar.DATE, 21)

        val year = tempCalendar.get(Calendar.YEAR)
        val month = tempCalendar.get(Calendar.MONTH) + 1
        val day = tempCalendar.get(Calendar.DAY_OF_MONTH)

        return getApiFormattedDate(year, month, day)
    }

    fun getDateRuFormat(date: String): String {

        val dateList = date.split(".")

        return "${dateList[2]}.${dateList[1]}.${dateList[0]}"
    }

    fun getApiFormattedDate(year: Int, month: Int, day: Int): String {

        return if(month < 10) {
            if (day < 10) {
                "$year.0$month.0$day"
            } else {
                "$year.0$month.$day"
            }
        }
        else {
            if(day < 10) {
                "$year.$month.0$day"
            } else {
                "$year.$month.$day"
            }
        }
    }

    fun getDayOfWeek(date: String): String {

        val tempCalendar = Calendar.getInstance()

        tempCalendar.time = dateFormat.parse(date)

        val monthName: String
        val dayName: String

        val month = tempCalendar.get(Calendar.MONTH) + 1
        val day = tempCalendar.get(Calendar.DAY_OF_WEEK)

        if (systemLanguage == "Russian") {
            monthName = MONTHS_RU[month-1]
            dayName = DAYS_RU[day-1]
        }
        else {
            monthName = MONTHS_EN[month-1]
            dayName = DAYS_EN[day-1]
        }

        return "$dayName, $day $monthName"
    }

    companion object {
        val MONTHS_RU = listOf("Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря")
        val MONTHS_EN = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        val DAYS_RU = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
        val DAYS_EN = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    }

}