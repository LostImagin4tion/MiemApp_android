import org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION as kotlinVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "ru.hse.miem.miemapp"
        minSdkVersion(22)
        targetSdkVersion(30)
        versionCode(1)
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    dynamicFeatures = mutableSetOf(":camerascontrol")
}

dependencies {
    implementation(kotlin("stdlib-jdk7", kotlinVersion))

    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android:flexbox:2.0.1")

    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    // TODO migrate to v2
    // MoxyX
    val moxyVersion = "1.7.0"
    implementation("tech.schoolhelper:moxy-x:$moxyVersion")
    implementation("tech.schoolhelper:moxy-x-androidx:$moxyVersion")
    kapt("tech.schoolhelper:moxy-x-compiler:$moxyVersion")

    // Android Jetpack Navigation
    val navigationVersion = "2.3.1"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // Glide
    val glideVersion = "4.10.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    // RxKotlin
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // Dagger 2
    val daggerVersion = "2.19"
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:2.19")
    kapt("com.google.dagger:dagger-compiler:2.19")
    kaptAndroidTest("com.google.dagger:dagger-compiler:2.19")

    // Retrofit 2
    val retrofitVersion = "2.8.1"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.5.0")

    // Google Auth
    implementation("com.google.android.gms:play-services-auth:19.0.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.2.1")

}