package ru.hse.miem.miemapp.presentation.vacancies


import android.util.Log
import moxy.InjectViewState
import moxy.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.domain.repositories.IVacancyRepository
import ru.hse.miem.miemapp.presentation.tinder.ViewAllView
import javax.inject.Inject

@InjectViewState
class VacanciesPresenter@Inject constructor(
    private val vacancyRepository: IVacancyRepository
) : MvpPresenter<ViewAllView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate() {
        Log.d("ViewAllLogs", "Create")
        val disposable = vacancyRepository.getAllVacancies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = viewState::setupLovedVacancies,
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