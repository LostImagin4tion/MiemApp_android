package ru.hse.miem.miemapp.presentation.project

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.presentation.base.BaseView

interface ProjectView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupProject(project: ProjectExtended)
}