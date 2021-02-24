package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import ru.hse.miem.miemapp.data.repositories.ProjectRepository
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.domain.repositories.ISearchRepository
import ru.hse.miem.miemapp.presentation.project.ProjectView
import ru.hse.miem.miemapp.presentation.search.SearchView
import javax.inject.Inject

class TinderPresenter @Inject constructor(
    private val searchRepository: ISearchRepository,
    private val projectRepository: IProjectRepository
) : MvpPresenter<SearchView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate() {
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
    }

    fun infoProject(projectId: Long) = projectRepository.getProjectById(projectId)
//    {
//        val tinderFragment: TinderFragment
//        val disposable = projectRepository.getProjectById(projectId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onSuccess =
//            )
//        compositeDisposable.add(disposable)
//    }
}