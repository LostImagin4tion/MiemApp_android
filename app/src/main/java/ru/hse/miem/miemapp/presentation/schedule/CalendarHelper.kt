package ru.hse.miem.miemapp.presentation.schedule

import java.time.Year
import java.util.*

class CalendarHelper {

    private val systemLanguage = Locale.getDefault().displayLanguage

    private val calendar = Calendar.getInstance()

    fun getCurrentDate(): String {

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$year.$month.$day"
    }

    fun getLastDate(currentDate: String): String {

        val tempCalendar = Calendar.getInstance()

        tempCalendar.timeInMillis = currentDate.toLong()

        val dayOfYear = tempCalendar.get(Calendar.DAY_OF_YEAR)
        tempCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear + 21)

        val year = tempCalendar.get(Calendar.YEAR)
        val month = tempCalendar.get(Calendar.MONTH)
        val day = tempCalendar.get(Calendar.DAY_OF_MONTH)

        return "$year.$month.$day"
    }

    fun getDayOfWeek(date: String): String {

        val tempCalendar = Calendar.getInstance()

        tempCalendar.timeInMillis = date.toLong()

        val monthName: String
        val dayName: String

        val month = tempCalendar.get(Calendar.MONTH)
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