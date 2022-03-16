package ru.hse.miem.miemapp.presentation.schedule

import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip
import ru.hse.miem.miemapp.domain.entities.ScheduleDay
import ru.hse.miem.miemapp.presentation.base.BaseView

interface ScheduleView : BaseView {
    @OneExecution fun setupSchedule(lessons: List<ScheduleDay>)

    @Skip fun updateSchedule(newDaysLesson: List<ScheduleDay>)
}