package ru.miem.indoor_dynamicfeature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.miem.indoor_sdk.IndoorManagerFactory

class IndoorActivity : AppCompatActivity() {

    private val indoorManager = IndoorManagerFactory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_layout, indoorManager.getIndoorFragment(), null)
            .commit()
    }
}