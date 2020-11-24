package ru.hse.miem.miemapp.presentation.search

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.presentation.base.BaseView

interface SearchView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupProjects(projects: List<ProjectInSearch>)
}