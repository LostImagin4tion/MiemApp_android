package ru.hse.miem.miemapp.presentation.tinder

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.base.BaseView
import ru.hse.miem.miemapp.presentation.project.ProjectView
import ru.hse.miem.miemapp.presentation.search.SearchView
import java.util.ArrayList

interface ViewAllView: BaseView {
    @AddToEndSingle
    fun setupLovedVacancies(vacancies: List<Vacancies>)
}