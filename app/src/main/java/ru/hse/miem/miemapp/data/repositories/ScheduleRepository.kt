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

    private fun ScheduleResponse.lessonsToDay(): List<ScheduleDay> {

        val scheduleDays: MutableList<ScheduleDay> = mutableListOf()

        scheduleDays.add(0, ScheduleDay(
            date = data[0].date,
            dayOfWeek = calendar.getDayOfWeek(data[0].date),
            lessons = mutableListOf()
        ))

        scheduleDays[0].lessons.add(data[0])

        for(i in data.indices) {

            val lastIndex = scheduleDays.lastIndex

            if (data[i].date == scheduleDays[lastIndex].date && i != 0) {
                scheduleDays[lastIndex].lessons.add(data[i])
            }
            else {
                scheduleDays.add(lastIndex + 1, ScheduleDay(
                    date = data[i].date,
                    dayOfWeek = calendar.getDayOfWeek(data[i].date),
                    lessons = mutableListOf()
                ))

                scheduleDays[lastIndex + 1].lessons.add(data[i])
            }
        }

        return scheduleDays
    }
}