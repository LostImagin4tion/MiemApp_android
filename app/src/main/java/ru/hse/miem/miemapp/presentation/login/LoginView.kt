package ru.hse.miem.miemapp.presentation.login

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemapp.presentation.base.BaseView

interface LoginView : BaseView {
    @OneExecution fun login()
    @OneExecution fun afterLogin()
    @AddToEndSingle fun showLoginButtons()
    @AddToEndSingle fun hideLoginButtons()
}