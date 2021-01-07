package ru.hse.miem.miemapp.presentation.base

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>() {

    protected fun <V> Observable<V>.proceedRequest(): Observable<V> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    protected fun <V> Single<V>.proceedRequest(): Single<V> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    protected fun Completable.proceedRequest(): Completable =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    protected fun proceedError(e: Throwable) {
        Log.w(javaClass.simpleName, e.stackTraceToString())
        viewState.showError()
    }
}