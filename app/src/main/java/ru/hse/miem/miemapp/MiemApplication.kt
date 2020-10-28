package ru.hse.miem.miemapp

import android.app.Application
import ru.hse.miem.miemapp.dagger.AppComponent
import ru.hse.miem.miemapp.dagger.DaggerAppComponent

class MiemApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
    }
}