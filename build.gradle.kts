// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by extra("1.4.10")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

// TODO add common dependencies file
subprojects {
    extra.apply {
        set("timber", "com.jakewharton.timber:timber:4.7.1")
    }
}


tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
