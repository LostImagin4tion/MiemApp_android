package ru.hse.miem.miemapp.presentation.apps

import android.graphics.drawable.Drawable
import androidx.navigation.NavDirections

data class AppItem(
    val icon: Drawable,
    val name: String,
    val isExternal: Boolean, // set "true" here, if app was integrated from another project
    val navigateAction: NavDirections? = null, // set navigation method from AppsFragmentDirection here, if app is internal
                                               // (e.g AppsFragmentDirections.actionFragmentAppsToFragmentSandbox())
    val activityClassName: String? = null // set full class name (with packages), if app is external (e.g com.some.example.ActivityName)
)