package ru.hse.miem.miemapp.presentation.base

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface BaseView : MvpView {
    @OneExecution fun showError()
}