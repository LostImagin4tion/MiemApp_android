package ru.hse.miem.miemapp.presentation.login

import android.content.Intent
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.hse.miem.miemapp.presentation.base.BaseView

interface LoginView : BaseView {
    @OneExecution fun login(signInIntent: Intent)
    @OneExecution fun afterLogin()
    @OneExecution fun showLoginForm()
    @AddToEndSingle fun showLoginButtons()
    @AddToEndSingle fun hideLoginButtons()
}