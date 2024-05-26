plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}

android {
    namespace = "${Config.namespace}.system"
    configureAndroidLibrary(this)
}

kotlin {
    configureMultiplatform(this, android = true)

    sourceSets.androidMain.dependencies {
        api(libs.androidx.core)
        api(projects.common)
        api(libs.androidx.activity)
    }
}

publishMultiplatform()
