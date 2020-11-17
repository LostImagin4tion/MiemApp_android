package ru.hse.miem.miemapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.repositories.IAuthRepository
import ru.hse.miem.miemapp.presentation.login.LoginFragment
import ru.hse.miem.miemapp.presentation.setupWithNavController
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var signInClient: GoogleSignInClient
    @Inject
    lateinit var authRepository: IAuthRepository
    private var authDisposable: Disposable? = null
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MiemApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInClient.silentSignIn().addOnCompleteListener {
            it.result?.serverAuthCode?.also { silentLogin(it) } ?: startLogin()
        }
    }

    private fun startLogin() {
        supportFragmentManager.beginTransaction()
            .add(R.id.navHost, loginFragment)
            .commit()
    }

    private fun silentLogin(authCode: String) {
        authDisposable = authRepository.auth(authCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.w("Auth", it.stackTraceToString())
                    startLogin()
                },
                onComplete = ::afterLogin
            )
    }

    fun afterLogin() {
        supportFragmentManager.beginTransaction()
            .remove(loginFragment)
            .commit()

        bottomNavigation.visibility = View.VISIBLE
        bottomNavigation.setupWithNavController(
            navGraphIds = listOf(R.navigation.nav_profile),
            fragmentManager = supportFragmentManager,
            containerId = R.id.navHost,
            intent = intent,
        )

    }

    override fun onStop() {
        super.onStop()
        authDisposable?.dispose()
    }
}