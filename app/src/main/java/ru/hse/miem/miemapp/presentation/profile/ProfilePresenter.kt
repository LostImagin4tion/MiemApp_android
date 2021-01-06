package ru.hse.miem.miemapp.presentation.profile

import android.util.Log
import moxy.InjectViewState
import moxy.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.hse.miem.miemapp.data.Session
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor(
    private val profileRepository: IProfileRepository,
    private val session: Session
) : MvpPresenter<ProfileView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate(userId: Long? = null, isTeacher: Boolean? = null) {
        val disposable = if (userId != null && isTeacher != null) {
                profileRepository.getProfileById(userId, isTeacher)
            } else {
                profileRepository.getMyProfile()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                    Log.w(javaClass.simpleName, it.stackTraceToString())
                    viewState.showError()
                }
            )

        compositeDisposable.add(disposable)
    }

    private fun loadProjects(userId: Long) {
        val disposable = profileRepository.getProjects(userId)
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

    private fun loadMyProjects() {
        val disposable = profileRepository.getMyProjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    viewState.setupMyProjects(it.projects)
                    viewState.setupMyApplications(it.applications)
                },
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