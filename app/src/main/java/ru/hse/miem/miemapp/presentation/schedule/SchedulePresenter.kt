package ru.hse.miem.miemapp.presentation.schedule

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.hse.miem.miemapp.domain.repositories.IScheduleRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class SchedulePresenter @Inject constructor(
    private val scheduleRepository: IScheduleRepository,
): BasePresenter<ScheduleView>() {

    fun onCreate(
        startDate: String,
        finishDate: String,
        isTeacher: Boolean
    ) = launch {
        try {
            scheduleRepository.getSchedule(
                startDate,
                finishDate,
                isTeacher
            ).let(viewState::setupSchedule)
        } catch (e: Exception) {
            proceedError(e)
        }
    }

    fun onScrolledDown(
        startDate: String,
        finishDate: String,
        isTeacher: Boolean
    ) = launch {
        try {
            scheduleRepository.getSchedule(
                startDate,
                finishDate,
                isTeacher
            ).let(viewState::updateSchedule)
        } catch (e: Exception) {
            proceedError(e)
        }
    }
}