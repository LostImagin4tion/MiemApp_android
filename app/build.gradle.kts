import ru.hse.miem.miemapp.build.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Versions.targetSdk)
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        applicationId = "ru.hse.miem.miemapp"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode(5)
        versionName = "1.2.1"
        project.base.archivesBaseName = "MiemApp-$versionName"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("./keystores/release.keystore")
            storePassword = "d9ZDgA-K!8CHzamme%4Sm2=^sSxe7!B-"
            keyAlias = "release"
            keyPassword = "d9ZDgA-K!8CHzamme%4Sm2=^sSxe7!B-"
        }
    }


    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }

    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
    dynamicFeatures = mutableSetOf(":camerascontrol")
}

dependencies {
    setupKotlin()
    setupAndroidCore()
    setupTimberLogging()

    setupTest()

    // Views and themes
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
    implementation("com.google.android:flexbox:${Versions.flexbox}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}")

    setupCoroutines()
    setupMoxy()
    setupJetpackNavigation()
    setupGlide()
    setupDagger()
    setupRetrofit()
    setupMarkwon()

    implementation("com.google.android.gms:play-services-auth:${Versions.playServices}")
    implementation("com.squareup.okhttp3:okhttp:${Versions.okHttp}")

// CardStackView
    val cardVersion = "2.3.4"
    implementation("com.yuyakaido.android:card-stack-view:${cardVersion}")

    implementation(project(":indoor:sdk"))
}