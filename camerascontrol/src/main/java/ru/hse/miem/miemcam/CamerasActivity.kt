package ru.hse.miem.miemcam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.data.Session
import javax.inject.Inject

class CamerasActivity : AppCompatActivity() {

    lateinit var camerasComponent: CamerasControlComponent

    @Inject
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        camerasComponent = DaggerCamerasControlComponent.builder()
            .appComponent((application as MiemApplication).appComponent)
            .build()

        camerasComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cameras_control)
    }
}