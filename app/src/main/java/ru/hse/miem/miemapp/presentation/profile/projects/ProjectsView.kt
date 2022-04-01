package ru.hse.miem.miemapp.presentation.profile.projects

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.MyProjectsAndApplications
import ru.hse.miem.miemapp.domain.entities.ProjectBasic
import ru.hse.miem.miemapp.presentation.base.BaseView

interface ProjectsView: BaseView {
    @AddToEndSingle fun setupProjects(projects: List<ProjectBasic>)
    @AddToEndSingle fun setupMyProjects(projects: List<MyProjectsAndApplications.MyProjectBasic>)
    @AddToEndSingle fun showNoDataFragment()
}