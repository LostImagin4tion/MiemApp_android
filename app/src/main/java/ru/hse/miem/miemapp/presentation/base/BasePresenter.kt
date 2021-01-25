package ru.hse.miem.miemapp.presentation.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import moxy.MvpPresenter
import timber.log.Timber

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    protected fun proceedError(e: Throwable) {
        Timber.w(e.stackTraceToString())
        viewState.showError()
    }
}