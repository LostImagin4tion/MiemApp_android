package ru.hse.miem.miemapp.presentation.schedule

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.domain.repositories.IScheduleRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class SchedulePresenter @Inject constructor(
    private val scheduleRepository: IScheduleRepository,
): BasePresenter<ScheduleView>() {

    fun onCreate(
        userId: String? = null,
        startDate: String,
        finishDate: String,
        isTeacher: Boolean? = null
    ) = launch {
        try {
            scheduleRepository.getSchedule(
                userId,
                startDate,
                finishDate,
                isTeacher
            ).let(viewState::setupSchedule)
        } catch (e: Exception) {
            proceedError(e)
        }
    }

    fun onScrolledDown(
        userId: String,
        startDate: String,
        finishDate: String,
        isTeacher: Boolean
    ) = launch {
        try {
            scheduleRepository.getSchedule(
                userId,
                startDate,
                finishDate,
                isTeacher
            ).let(viewState::updateScheduleDays)
        } catch (e: Exception) {
            proceedError(e)
        }
    }
}