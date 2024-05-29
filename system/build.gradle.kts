plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}

kotlin {
    configureMultiplatform(
        this,
        android = true,
        jvm = false,
        linux = false,
        js = false,
        tvOs = false,
        iOs = false,
        macOs = false,
        watchOs = false,
        windows = false,
        wasmWasi = false,
        wasmJs = false
    )

    sourceSets.androidMain.dependencies {
        api(libs.androidx.core)
        api(projects.common)
        api(libs.androidx.activity)
    }
}

android {
    namespace = "${Config.namespace}.system"
    configureAndroidLibrary(this)
}

publishMultiplatform()
