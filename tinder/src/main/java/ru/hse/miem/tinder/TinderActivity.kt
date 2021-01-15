package ru.hse.miem.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.tinder.dagger.DaggerTinderComponent
import ru.hse.miem.tinder.dagger.TinderComponent

class TinderActivity : AppCompatActivity() {

    lateinit var tinderComponent: TinderComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLogs", "OK")
        tinderComponent = DaggerTinderComponent.builder()
            .appComponent((application as MiemApplication).appComponent)
            .build()

        tinderComponent.inject(this)
        super.onCreate(savedInstanceState)
        Log.d("MyLogs", "OK1")
        setContentView(R.layout.activity_tinder)

    }
}