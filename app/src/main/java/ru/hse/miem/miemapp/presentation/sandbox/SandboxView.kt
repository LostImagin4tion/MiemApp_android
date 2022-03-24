package ru.hse.miem.miemapp.presentation.sandbox

import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemapp.domain.entities.ProjectInSandbox
import ru.hse.miem.miemapp.presentation.base.BaseView

interface SandboxView: BaseView {

    @OneExecution fun setupSandbox(projects: List<ProjectInSandbox>)
}