import ru.hse.miem.miemapp.build.Versions
import ru.hse.miem.miemapp.build.*

plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Versions.targetSdk)
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }

    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
}

dependencies {
    implementation(project(":app"))

    setupKotlin()
    setupAndroidCore()
    setupTimberLogging()

    setupTest()

    // Views and themes
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}")

    setupMoxy()
    setupDagger()

    // RxKotlin
    implementation("io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}")
    implementation("io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}")

    setupRetrofit()
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Versions.rxJavaAdapter}")
}