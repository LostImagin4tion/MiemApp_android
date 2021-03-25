package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import ru.hse.miem.miemapp.domain.repositories.IVacancyRepository
import javax.inject.Inject

class TinderPresenter @Inject constructor(
    private val vacancyRepository: IVacancyRepository
) : MvpPresenter<InfoView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate() {
        Log.d("TinderMyLogs", "Create")
        val disposable = vacancyRepository.getAllVacancies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = viewState::setupVacancies,
                onError = {
                    Log.w(javaClass.simpleName, it.stackTraceToString())
                    viewState.showError()
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}