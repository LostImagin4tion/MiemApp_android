package ru.hse.miem.miemapp.presentation.profile.git_stats

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.hse.miem.miemapp.domain.entities.UserGitStatistics
import ru.hse.miem.miemapp.presentation.base.BaseView

interface GitStatsView: BaseView {
    @AddToEndSingle fun setupGitStats(gitStats: List<UserGitStatistics>)
}