package ru.hse.miem.miemapp.presentation.login

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface LoginView : MvpView {

    @OneExecution fun login()
    @OneExecution fun afterLogin()
    @AddToEndSingle fun showLoginButton()
    @AddToEndSingle fun hideLoginButton()
}