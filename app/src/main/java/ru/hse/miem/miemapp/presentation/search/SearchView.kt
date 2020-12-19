package ru.hse.miem.miemapp.presentation.search

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.presentation.base.BaseView

interface SearchView : BaseView {
    @AddToEndSingle fun setupProjects(projects: List<ProjectInSearch>)
}