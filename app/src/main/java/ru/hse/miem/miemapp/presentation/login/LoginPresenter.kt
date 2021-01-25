package ru.hse.miem.miemapp.presentation.login

import moxy.InjectViewState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.RuntimeExecutionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.hse.miem.miemapp.domain.repositories.IAuthRepository
import ru.hse.miem.miemapp.presentation.base.BasePresenter
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class LoginPresenter @Inject constructor(
    private val authRepository: IAuthRepository,
    private val signInClient: GoogleSignInClient
) : BasePresenter<LoginView>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    fun onCreate() {
        signInClient.silentSignIn().addOnCompleteListener {
            try {
                it.result?.serverAuthCode?.also(::onLogged) ?: viewState.showLoginForm()
            } catch (e: RuntimeExecutionException) { // api exception
                Timber.w(e.stackTraceToString())
                viewState.showLoginForm()
            }
        }
    }

    fun onClickLoginButton() {
        viewState.hideLoginButtons()
        viewState.login(signInClient.signInIntent)
    }

    fun onLogged(authCode: String) = launch {
        try {
            authRepository.auth(authCode)
            viewState.afterLogin()
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
        }
    }

}