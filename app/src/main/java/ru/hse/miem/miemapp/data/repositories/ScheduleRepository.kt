package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.data.api.RuzApi
import ru.hse.miem.miemapp.data.api.ScheduleResponse
import ru.hse.miem.miemapp.domain.entities.ScheduleDay
import ru.hse.miem.miemapp.domain.repositories.IScheduleRepository
import ru.hse.miem.miemapp.presentation.schedule.CalendarHelper
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val cabinetApi: CabinetApi,
    private val ruzApi: RuzApi
): IScheduleRepository {

    private val calendar = CalendarHelper()

    override suspend fun getSchedule(
        userId: String,
        startDate: String,
        finishDate: String,
        isTeacher: Boolean
    ) = withIO {
        if (!isTeacher) {

            val googleEmail = cabinetApi.myStudentProfile().data[0].main.email
            val eduEmail = cabinetApi.userInfoByEmail(googleEmail).data.emailsList[0]

            ruzApi.studentSchedule(
                eduEmail,
                startDate,
                finishDate
            ).lessonsToDay()
        }
        else {

            val googleEmail = cabinetApi.myTeacherProfile().data[0].main.email
            val eduEmail = cabinetApi.userInfoByEmail(googleEmail).data.emailsList[0]

            ruzApi.staffSchedule(
                eduEmail,
                startDate,
                finishDate
            ).lessonsToDay()
        }
    }

    private fun List<ScheduleResponse>.lessonsToDay(): List<ScheduleDay> {

        val scheduleDays: MutableList<ScheduleDay> = mutableListOf()

        scheduleDays.add(0, ScheduleDay(
            date = this[0].date,
            dayOfWeek = calendar.getDayOfWeek(this[0].date),
            lessons = mutableListOf()
        ))

        for(i in this.indices) {

            val lastIndex = scheduleDays.lastIndex

            if (this[i].date == scheduleDays[lastIndex].date) {
                scheduleDays[lastIndex].lessons.add(this[i])
            }
            else {
                scheduleDays.add(lastIndex + 1, ScheduleDay(
                    date = this[i].date,
                    dayOfWeek = calendar.getDayOfWeek(this[i].date),
                    lessons = mutableListOf()
                ))

                scheduleDays[lastIndex + 1].lessons.add(this[i])
            }
        }

        return scheduleDays
    }
}