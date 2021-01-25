package ru.hse.miem.miemapp

import android.app.Application
import android.util.Log
import ru.hse.miem.miemapp.dagger.AppComponent
import ru.hse.miem.miemapp.dagger.DaggerAppComponent
import ru.hse.miem.miemapp.utils.FileLoggingTree
import timber.log.Timber

class MiemApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()

        // logging configs
        val minLoggingPriority = if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Log.DEBUG
        } else {
            Log.WARN
        }
        try {
            Timber.plant(FileLoggingTree(applicationContext.getExternalFilesDir("logs")!!.absolutePath, minLoggingPriority))
        } catch (e: NullPointerException) {
            Timber.w("Cannot setup FileLoggingTree, skipping")
        }
    }
}