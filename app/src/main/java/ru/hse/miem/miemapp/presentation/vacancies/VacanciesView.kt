package ru.hse.miem.miemapp.presentation.vacancies

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.base.BaseView

interface VacanciesView : BaseView {
    @AddToEndSingle
    fun setupLovedVacancies(vacancies: List<Vacancies>)
}