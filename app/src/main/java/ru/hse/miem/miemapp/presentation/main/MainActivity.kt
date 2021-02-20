package ru.hse.miem.miemapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.RuntimeExecutionException
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
        setTheme(R.style.Theme_MIEMApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setBackgroundDrawableResource(R.drawable.splash_screen)

        if (savedInstanceState == null) { // not screen rotation or something like this
            signInClient.silentSignIn().addOnCompleteListener {
                try {
                    it.result?.serverAuthCode?.also { silentLogin(it) } ?: startLogin()
                } catch (e: RuntimeExecutionException) { // api exception
                    Log.w(javaClass.simpleName, e.stackTraceToString())
                    startLogin()
                }
            }
        } else {
            afterLogin()
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
        window.setBackgroundDrawableResource(R.drawable.solid_color_primary)

        supportFragmentManager.beginTransaction()
            .remove(loginFragment)
            .commit()

        bottomNavigation.visibility = View.VISIBLE
        bottomNavigation.setupWithNavController(
            navGraphIds = listOf(R.navigation.nav_profile, R.navigation.nav_search, R.navigation.nav_settings, R.navigation.nav_apps, R.navigation.nav_tinder),
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