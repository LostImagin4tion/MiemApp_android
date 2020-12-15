package ru.hse.miem.miemapp.presentation.apps

import android.graphics.drawable.Drawable

data class AppItem (
    val icon: Drawable,
    val name: String,
    val activityClassName: String // full class name (with packages). e.g com.some.example.ActivityName
)