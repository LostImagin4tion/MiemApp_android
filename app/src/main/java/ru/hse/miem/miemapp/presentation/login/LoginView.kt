package ru.hse.miem.miemapp.presentation.login

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface LoginView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun login()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToMainScreen()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoginButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideLoginButton()
}