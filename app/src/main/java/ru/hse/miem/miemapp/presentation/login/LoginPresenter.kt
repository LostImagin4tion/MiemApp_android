package ru.hse.miem.miemapp.presentation.login

import android.util.Log
import moxy.InjectViewState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.hse.miem.miemapp.domain.repositories.IAuthRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class LoginPresenter @Inject constructor(
    private val authRepository: IAuthRepository
) : BasePresenter<LoginView>() {

    private val compositeDisposable = CompositeDisposable()

    fun onClickLoginButton() {
        viewState.hideLoginButtons()
        viewState.login()
    }

    fun onLogged(googleSignInAccount: GoogleSignInAccount) {
        val disposable = authRepository.auth(googleSignInAccount.serverAuthCode!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = viewState::afterLogin,
                onError = { Log.e("Auth", it.stackTraceToString()) }
            )
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}