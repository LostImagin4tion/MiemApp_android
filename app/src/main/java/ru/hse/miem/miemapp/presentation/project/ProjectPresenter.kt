package ru.hse.miem.miemapp.presentation.project

import android.util.Log
import io.reactivex.Completable
import moxy.InjectViewState
import moxy.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import javax.inject.Inject

@InjectViewState
class ProjectPresenter @Inject constructor(
    private val projectRepository: IProjectRepository
) : MvpPresenter<ProjectView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate(projectId: Long) {
        val disposable = projectRepository.getProjectById(projectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = viewState::setupProject,
                onError = {
                    Log.w(javaClass.simpleName, it.stackTraceToString())
                    viewState.showError()
                }
            )
        compositeDisposable.add(disposable)
    }

    fun onSubmitVacancyApplication(vacancyId: Long, aboutMe: String): Completable = projectRepository.applyForVacancy(vacancyId, aboutMe)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .doOnError {
               Log.w(javaClass.simpleName, it.stackTraceToString())
               viewState.showError()
           }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}