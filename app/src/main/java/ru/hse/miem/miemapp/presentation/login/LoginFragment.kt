package ru.hse.miem.miemapp.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.presentation.main.MainActivity
import javax.inject.Inject

class LoginFragment : MvpAppCompatFragment(), LoginView {

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 42
    }

    @Inject
    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    @ProvidePresenter
    fun provideLoginPresenter() = loginPresenter

    @Inject
    lateinit var signInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginProgress.visibility = View.INVISIBLE
        googleSignInButton.visibility = View.VISIBLE
        googleSignInButton.setOnClickListener {
            loginPresenter.onClickLoginButton()
        }
    }

    override fun navigateToMainScreen() {
        (activity as MainActivity).let {
            it.supportFragmentManager.popBackStack()
            // it.setupBottomNavigationBar()
        }
    }

    override fun login() {
        val intent = signInClient.signInIntent
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
    }

    override fun showLoginButton() {
        loginProgress.visibility = View.INVISIBLE
        googleSignInButton.visibility = View.VISIBLE
        loginErrorText.visibility = View.VISIBLE
    }

    override fun hideLoginButton() {
        googleSignInButton.visibility = View.INVISIBLE
        loginErrorText.visibility = View.INVISIBLE
        loginProgress.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            try {
                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)?.let {
                    loginPresenter.onLogged(it)
                }
            } catch (e: ApiException) {
                Log.e("Login", e.message)
                showLoginButton()
            }
        }
    }
}
