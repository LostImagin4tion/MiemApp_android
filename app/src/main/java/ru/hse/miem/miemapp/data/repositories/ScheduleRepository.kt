package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.api.RuzApi
import ru.hse.miem.miemapp.data.api.ScheduleResponse
import ru.hse.miem.miemapp.domain.entities.*
import ru.hse.miem.miemapp.domain.repositories.IScheduleRepository
import ru.hse.miem.miemapp.presentation.schedule.CalendarHelper
import java.util.*
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val cabinetApi: CabinetApi,
    private val ruzApi: RuzApi
): IScheduleRepository {

    private val calendar = CalendarHelper()

    override suspend fun getSchedule(
        startDate: String,
        finishDate: String,
        isTeacher: Boolean
    ) = withIO {

        val systemLang = when(Locale.getDefault().displayLanguage) {
            "Russian" -> "1"
            else -> "2"
        }

        if (!isTeacher) {

            val googleEmail = cabinetApi.myStudentProfile().data[0].main.email
            val eduEmail = cabinetApi.userInfoByEmail(googleEmail).data.emailsList[0]

            ruzApi.studentSchedule(
                eduEmail,
                startDate,
                finishDate,
                systemLang
            ).sorted().lessonsToDay()
        }
        else {

            val googleEmail = cabinetApi.myTeacherProfile().data[0].main.email
            val eduEmail = cabinetApi.userInfoByEmail(googleEmail).data.emailsList[0]

            ruzApi.staffSchedule(
                eduEmail,
                startDate,
                finishDate,
                systemLang
            ).sorted().lessonsToDay()
        }
    }

    private fun List<ScheduleResponse>.sorted(): List<ScheduleResponse> {

        if(this.size < 2) {
            return this
        }
        val pivot = this[this.size/2]

        val equalElements = this.filter { it.date == pivot.date }
        val lessElements = this.filter { it.date < pivot.date }
        val greaterElements = this.filter { it.date > pivot.date }

        return lessElements.sorted() + equalElements + greaterElements.sorted()
    }

    private fun List<ScheduleResponse>.lessonsToDay(): MutableList<IScheduleItem> {
        if(this.isEmpty()) {
            return mutableListOf()
        }

        val scheduleItems: MutableList<IScheduleItem> = mutableListOf()

        scheduleItems.add(0, ScheduleDayName(
            date = this[0].date,
            dayOfWeek = calendar.getDayOfWeek(this[0].date)
        ))

        for(i in this.indices) {

            val lastIndex = scheduleItems.lastIndex
            val dayOfWeek = calendar.getDayOfWeek(this[i].date)

            if(scheduleItems[lastIndex] is ScheduleDayName) {

                scheduleItems.add(ScheduleDayLesson(
                    date = this[i].date,
                    dayOfWeek = dayOfWeek,
                    lesson = this[i]
                ))
            }
            else {
                if((scheduleItems[lastIndex] as ScheduleDayLesson).date == this[i].date) {

                    scheduleItems.add(ScheduleDayLesson(
                        date = this[i].date,
                        dayOfWeek = dayOfWeek,
                        lesson = this[i]
                    ))
                }
                else {
                    scheduleItems.add(ScheduleDayName(
                        date = this[i].date,
                        dayOfWeek = dayOfWeek,
                    ))
                    scheduleItems.add(ScheduleDayLesson(
                        date = this[i].date,
                        dayOfWeek = dayOfWeek,
                        lesson = this[i]
                    ))
                }
            }
        }

        return scheduleItems
    }
}