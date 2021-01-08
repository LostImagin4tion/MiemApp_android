package ru.hse.miem.miemapp.presentation.profile

import moxy.InjectViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import ru.hse.miem.miemapp.data.Session
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor(
    private val profileRepository: IProfileRepository,
    private val session: Session
) : BasePresenter<ProfileView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate(userId: Long? = null, isTeacher: Boolean? = null) {
        val disposable = if (userId != null && isTeacher != null) {
                profileRepository.getProfileById(userId, isTeacher)
            } else {
                profileRepository.getMyProfile()
            }
            .proceedRequest()
            .subscribeBy(
                onSuccess = {
                    viewState.setupProfile(it)

                    // because idk how to show my profile for teacher
                    // FIXME maybe i'll investigate
                    if (userId == null && session.isStudent) {
                        loadMyProjects()
                    } else {
                        loadProjects(it.id)
                    }
                },
                onError = {
                    proceedError(it)
                    viewState.showUnauthorizedProfile()
                }
            )

        compositeDisposable.add(disposable)
    }

    private fun loadProjects(userId: Long) {
        val disposable = profileRepository.getProjects(userId)
            .proceedRequest()
            .subscribeBy(
                onSuccess = viewState::setupProjects,
                onError = ::proceedError
            )
        compositeDisposable.add(disposable)
    }

    private fun loadMyProjects() {
        val disposable = profileRepository.getMyProjectsAndApplications()
            .proceedRequest()
            .subscribeBy(
                onSuccess = {
                    viewState.setupMyProjects(it.projects)
                    viewState.setupMyApplications(it.applications)
                },
                onError = ::proceedError
            )
        compositeDisposable.add(disposable)
    }

    fun onApproveApplication(applicationId: Long) {
        val disposable = profileRepository.applicationApprove(applicationId)
            .proceedRequest()
            .doFinally { loadMyProjects() }
            .subscribeBy(::proceedError)
        compositeDisposable.add(disposable)
    }

    fun onWithdrawApplication(applicationId: Long) {
        val disposable = profileRepository.applicationWithdraw(applicationId)
            .proceedRequest()
            .doFinally { loadMyProjects() }
            .subscribeBy(::proceedError)
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}