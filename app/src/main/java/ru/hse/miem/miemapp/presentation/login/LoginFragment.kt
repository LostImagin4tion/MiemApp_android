package ru.hse.miem.miemapp.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.main.MainActivity
import timber.log.Timber
import javax.inject.Inject

class LoginFragment : BaseFragment(R.layout.fragment_login), LoginView {

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 42
    }

    @Inject
    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    @ProvidePresenter
    fun provideLoginPresenter() = loginPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setBackgroundDrawableResource(R.drawable.solid_color_accent)

        googleSignInButton.setOnClickListener { loginPresenter.onClickLoginButton() }
        withoutAuthButton.setOnClickListener { afterLogin() }
        loginPresenter.onCreate()
    }

    override fun showLoginForm() {
        loginProgress.visibility = View.INVISIBLE
        loginButtons.visibility = View.VISIBLE
    }

    override fun afterLogin() {
        (requireActivity() as MainActivity).let {
            findNavController().apply {
                it.intentUri?.also(::navigate) ?: navigate(R.id.action_fragmentLogin_to_fragmentProfile)
                it.window.setBackgroundDrawableResource(R.drawable.solid_color_primary)
                graph.startDestination = R.id.fragmentProfile
            }
        }
    }

    override fun login(signInIntent: Intent) {
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    override fun showLoginButtons() {
        loginProgress.visibility = View.INVISIBLE
        loginButtons.visibility = View.VISIBLE
        loginErrorText.visibility = View.VISIBLE
    }

    override fun hideLoginButtons() {
        loginButtons.visibility = View.INVISIBLE
        loginErrorText.visibility = View.INVISIBLE
        loginProgress.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            try {
                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)?.let {
                    loginPresenter.onLogged(it.serverAuthCode!!)
                }
            } catch (e: ApiException) {
                Timber.e(e.message.toString())
                showLoginButtons()
            }
        }
    }
}
