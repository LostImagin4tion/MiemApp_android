package ru.hse.miem.miemapp.presentation.search

import moxy.InjectViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import ru.hse.miem.miemapp.domain.repositories.ISearchRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val searchRepository: ISearchRepository
) : BasePresenter<SearchView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate() {
        val disposable = searchRepository.getAllProjects()
            .proceedRequest()
            .subscribeBy(
                onSuccess = viewState::setupProjects,
                onError = ::proceedError
            )
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}