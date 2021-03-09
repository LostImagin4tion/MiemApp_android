package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.domain.repositories.ISearchRepository
import javax.inject.Inject

class TinderPresenter @Inject constructor(
    private val searchRepository: ISearchRepository,
    private val projectRepository: IProjectRepository
) : MvpPresenter<InfoView>() {

    private val compositeDisposable = CompositeDisposable()
    private val compositeDisposable1 = CompositeDisposable()

    fun onCreate() {
        Log.d("TinderMyLogs", "Create")
        val disposable = searchRepository.getAllProjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = viewState::setupProjects,
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
        compositeDisposable1.dispose()
    }

    fun infoProject(projectId: Long)
    {
        Log.d("TinderMyLogs", "Info")
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
        compositeDisposable1.add(disposable)
    }
}