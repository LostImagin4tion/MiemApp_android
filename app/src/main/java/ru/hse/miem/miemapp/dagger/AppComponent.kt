package ru.hse.miem.miemapp.dagger

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.hse.miem.miemapp.presentation.login.LoginFragment
import ru.hse.miem.miemapp.presentation.main.MainActivity
import ru.hse.miem.miemapp.presentation.profile.ProfileFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [GoogleModule::class, RepositoryModule::class, DataModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(profileFragment: ProfileFragment)
}