package ru.hse.miem.miemapp.presentation.profile.applications

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.MyProjectsAndApplications
import ru.hse.miem.miemapp.presentation.base.BaseView

interface MyApplicationsView: BaseView {
    @AddToEndSingle fun setupMyApplications(applications: List<MyProjectsAndApplications.MyApplication>)
    @AddToEndSingle fun showNoDataFragment()
}