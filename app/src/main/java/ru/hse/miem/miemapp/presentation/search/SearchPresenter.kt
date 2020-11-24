package ru.hse.miem.miemapp.presentation.search

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.hse.miem.miemapp.domain.repositories.ISearchRepository
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val searchRepository: ISearchRepository
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
}