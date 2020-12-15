import org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION as kotlinVersion

plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(30)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":app"))

    implementation(kotlin("stdlib-jdk7", kotlinVersion))
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    // MoxyX
    val moxyVersion = "1.7.0"
    implementation("tech.schoolhelper:moxy-x:$moxyVersion")
    implementation("tech.schoolhelper:moxy-x-androidx:$moxyVersion")
    kapt("tech.schoolhelper:moxy-x-compiler:$moxyVersion")

    // Dagger 2
    val daggerVersion = "2.19"
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kaptAndroidTest("com.google.dagger:dagger-compiler:$daggerVersion")

    // RxKotlin
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // Retrofit 2
    val retrofitVersion = "2.8.1"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.5.0")
}