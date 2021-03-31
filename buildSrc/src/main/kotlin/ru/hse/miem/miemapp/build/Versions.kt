package ru.hse.miem.miemapp.build

import org.gradle.api.JavaVersion

object Versions {
    // languages
    const val kotlin = "1.4.10"
    val javaVersion = JavaVersion.VERSION_1_8
    const val jvmTarget = "1.8"

    // sdk
    const val targetSdk = 30
    const val minSdk = 22
    const val buildTools = "30.0.3"

    // libraries
    const val androidXCoreKtx = "1.3.2"
    const val androidXAppCompat = "1.2.0"

    const val material = "1.2.1"

    const val constraintLayout = "2.0.4"
    const val flexbox = "2.0.1"
    const val swipeRefreshLayout = "1.1.0"

    const val junit = "4.13.1"
    const val androidXExtJunit = "1.1.2"
    const val espressoCore = "3.3.0"

    const val coroutines = "1.4.2"

    const val moxy = "2.2.1"

    const val jetpackNavigation = "2.3.1"

    const val glide = "4.10.0"

    const val dagger = "2.19"

    const val retrofit = "2.8.1"

    const val playServices = "19.0.0"

    const val okHttp = "4.2.1"

    const val markwon = "4.6.1"

    const val timber = "4.7.1"

    const val rxKotlin = "2.4.0"
    const val rxAndroid = "2.1.1"
    const val rxJavaAdapter = "2.5.0"
}