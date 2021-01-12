package ru.hse.miem.miemapp.presentation.base

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import moxy.MvpPresenter

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    protected fun proceedError(e: Throwable) {
        Log.w(javaClass.simpleName, e.stackTraceToString())
        viewState.showError()
    }
}