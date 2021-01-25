package ru.hse.miem.miemapp

import android.app.Application
import android.util.Log
import androidx.core.net.toUri
import ru.hse.miem.miemapp.dagger.AppComponent
import ru.hse.miem.miemapp.dagger.DaggerAppComponent
import ru.hse.miem.miemapp.utils.FileLoggingTree
import timber.log.Timber

class MiemApplication : Application() {
    lateinit var appComponent: AppComponent

    // logging
    private var fileLoggingTree: FileLoggingTree? = null
    val currentLogFileUri get() = fileLoggingTree?.currentFile?.toUri()

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
            fileLoggingTree = FileLoggingTree(applicationContext.getExternalFilesDir("logs")!!.absolutePath, minLoggingPriority)
            Timber.plant(fileLoggingTree!!)
        } catch (e: NullPointerException) {
            Timber.w("Cannot setup FileLoggingTree, skipping")
        }
    }

    companion object {
        const val DEVELOPER_MAIL = "app@miem.hse.ru"
    }
}