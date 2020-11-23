package ru.hse.miem.miemapp.presentation.profile

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.hse.miem.miemapp.domain.repositories.IProfileRepository
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor(
    private val profileRepository: IProfileRepository
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
                .map {
                    viewState.setupProfile(it)
                    it
                }
                .observeOn(Schedulers.io())
                .flatMap { profileRepository.getProjects(it.id) }
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