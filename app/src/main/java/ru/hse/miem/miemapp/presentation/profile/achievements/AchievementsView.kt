package ru.hse.miem.miemapp.presentation.profile.achievements

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.Achievements
import ru.hse.miem.miemapp.presentation.base.BaseView

interface AchievementsView: BaseView {
    @AddToEndSingle fun setupAchievements(achievements: Achievements)
    @AddToEndSingle fun showNoDataTrackingSection()
    @AddToEndSingle fun showNoDataGitlabSection()
}