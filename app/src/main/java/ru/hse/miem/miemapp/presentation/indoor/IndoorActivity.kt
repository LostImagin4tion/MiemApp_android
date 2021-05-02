package ru.hse.miem.miemapp.presentation.indoor

import ru.hse.miem.miemapp.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.miem.indoor_sdk.IndoorManagerFactory

class IndoorActivity : AppCompatActivity() {

    // это наверн лучше в даггер вынести
    // написал так для наглядности
    private val indoorManager = IndoorManagerFactory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indoor)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_layout, indoorManager.getIndoorFragment(), null)
            .commit()
    }
}