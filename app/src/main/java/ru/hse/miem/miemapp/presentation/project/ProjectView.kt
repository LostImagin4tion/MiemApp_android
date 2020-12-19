package ru.hse.miem.miemapp.presentation.project

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.presentation.base.BaseView

interface ProjectView : BaseView {
    @AddToEndSingle fun setupProject(project: ProjectExtended)
}