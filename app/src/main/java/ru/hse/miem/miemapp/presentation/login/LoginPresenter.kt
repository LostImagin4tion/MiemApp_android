package ru.hse.miem.miemapp.presentation.login

import android.util.Log
import moxy.InjectViewState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.domain.repositories.IAuthRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class LoginPresenter @Inject constructor(
    private val authRepository: IAuthRepository
) : BasePresenter<LoginView>() {

    fun onClickLoginButton() {
        viewState.hideLoginButtons()
        viewState.login()
    }

    fun onLogged(googleSignInAccount: GoogleSignInAccount) = launch {
        try {
            authRepository.auth(googleSignInAccount.serverAuthCode!!)
            viewState.afterLogin()
        } catch (e: Exception) {
            Log.e("Auth", e.stackTraceToString())
        }
    }

}