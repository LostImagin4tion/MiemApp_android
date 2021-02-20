package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.presentation.project.ProjectView
import javax.inject.Inject

class TinderPresenter @Inject constructor(
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}