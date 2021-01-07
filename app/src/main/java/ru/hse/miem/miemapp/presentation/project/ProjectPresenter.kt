package ru.hse.miem.miemapp.presentation.project

import io.reactivex.Completable
import moxy.InjectViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class ProjectPresenter @Inject constructor(
    private val projectRepository: IProjectRepository
) : BasePresenter<ProjectView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate(projectId: Long) {
        val disposable = projectRepository.getProjectById(projectId)
            .proceedRequest()
            .subscribeBy(
                onSuccess = viewState::setupProject,
                onError = ::proceedError
            )
        compositeDisposable.add(disposable)
    }

    fun onSubmitVacancyApplication(vacancyId: Long, aboutMe: String): Completable = projectRepository.applyForVacancy(vacancyId, aboutMe)
           .proceedRequest()
           .doOnError(::proceedError)

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}