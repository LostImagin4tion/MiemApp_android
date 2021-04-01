package ru.hse.miem.miemapp.presentation.tinder

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.presentation.base.BaseView

interface InfoView : BaseView {
    @AddToEndSingle
    fun setupVacancies(projects: List<Vacancies>)
}