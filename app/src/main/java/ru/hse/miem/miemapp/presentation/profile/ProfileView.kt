package ru.hse.miem.miemapp.presentation.profile

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.hse.miem.miemapp.domain.entities.Profile

interface ProfileView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupProfile(profile: Profile)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError()
}