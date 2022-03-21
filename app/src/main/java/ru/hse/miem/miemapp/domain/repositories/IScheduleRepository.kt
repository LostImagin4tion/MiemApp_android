package ru.hse.miem.miemapp.domain.repositories

import ru.hse.miem.miemapp.domain.entities.IScheduleItem

interface IScheduleRepository {
    suspend fun getSchedule(
        userId: String,
        startDate: String,
        finishDate: String,
        isTeacher: Boolean
    ): List<IScheduleItem>
}