package ru.hse.miem.miemapp.presentation.schedule

import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemapp.domain.entities.IScheduleItem
import ru.hse.miem.miemapp.presentation.base.BaseView

interface ScheduleView : BaseView {
    @OneExecution fun setupSchedule(items: List<IScheduleItem>)

    @OneExecution fun updateSchedule(newItems: List<IScheduleItem>)
}