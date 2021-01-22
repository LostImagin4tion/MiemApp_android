package ru.hse.miem.miemapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.presentation.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MiemApplication).appComponent.inject(this)
        setTheme(R.style.Theme_MIEMApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) { // not screen rotation or something like this
            bottomNavigation.setupWithNavController(
                navGraphIds = listOf(
                    R.navigation.nav_search,
                    R.navigation.nav_settings,
                    R.navigation.nav_apps,
                    R.navigation.nav_profile // keep nav_profile last
                ),
                fragmentManager = supportFragmentManager,
                containerId = R.id.navHost,
                intent = intent,
                fragmentsBarGone = listOf(R.id.fragmentLogin)
            )
        }
    }
}