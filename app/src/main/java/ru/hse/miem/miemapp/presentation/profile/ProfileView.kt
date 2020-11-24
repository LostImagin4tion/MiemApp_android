package ru.hse.miem.miemapp.presentation.profile

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.hse.miem.miemapp.domain.entities.Profile
import ru.hse.miem.miemapp.domain.entities.ProjectBasic
import ru.hse.miem.miemapp.presentation.base.BaseView

interface ProfileView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupProfile(profile: Profile)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupProjects(projects: List<ProjectBasic>)
}