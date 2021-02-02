package ru.hse.miem.miemapp.build

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.kotlin

private const val implementation = "implementation"
private const val kapt = "kapt"

fun DependencyHandlerScope.setupKotlin() {
    implementation(kotlin("stdlib-jdk8", Versions.kotlin))
}

fun DependencyHandlerScope.setupAndroidCore() {
    implementation("androidx.core:core-ktx:${Versions.androidXCoreKtx}")
    implementation("androidx.appcompat:appcompat:${Versions.androidXAppCompat}")
}

fun DependencyHandlerScope.setupTest() {
    "testImplementation"("junit:junit:${Versions.junit}")
    "androidTestImplementation"("androidx.test.ext:junit:${Versions.androidXExtJunit}")
    "androidTestImplementation"("androidx.test.espresso:espresso-core:${Versions.espressoCore}")
}

fun DependencyHandlerScope.setupTimberLogging() {
    implementation("com.jakewharton.timber:timber:${Versions.timber}")
}

fun DependencyHandlerScope.setupCoroutines() {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
}

fun DependencyHandlerScope.setupMoxy() {
    implementation("com.github.moxy-community:moxy:${Versions.moxy}")
    implementation("com.github.moxy-community:moxy-androidx:${Versions.moxy}")
    kapt("com.github.moxy-community:moxy-compiler:${Versions.moxy}")
}

fun DependencyHandlerScope.setupJetpackNavigation() {
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.jetpackNavigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.jetpackNavigation}")
}

fun DependencyHandlerScope.setupGlide() {
    implementation("com.github.bumptech.glide:glide:${Versions.glide}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glide}")
}

fun DependencyHandlerScope.setupDagger() {
    implementation("com.google.dagger:dagger-android:${Versions.dagger}")
    kapt("com.google.dagger:dagger-android-processor:${Versions.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Versions.dagger}")
    "kaptAndroidTest"("com.google.dagger:dagger-compiler:${Versions.dagger}")
}

fun DependencyHandlerScope.setupRetrofit() {
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofit}")
}

fun DependencyHandlerScope.setupMarkwon() {
    implementation("io.noties.markwon:core:${Versions.markwon}")
    implementation("io.noties.markwon:image-glide:${Versions.markwon}")
}
