package ru.hse.miem.miemapp.presentation.project

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.hse.miem.miemapp.domain.entities.ProjectExtended

interface ProjectView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupProject(project: ProjectExtended)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError()
}