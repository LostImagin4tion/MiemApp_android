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

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }
}

dependencies {
    implementation(project(":app"))
    implementation(project(":indoor:sdk"))

    setupKotlin()
    setupAndroidCore()
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
}
