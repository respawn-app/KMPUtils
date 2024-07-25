plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    signing
}

kotlin {
    configureMultiplatform(this, android = true)

    sourceSets.androidMain.dependencies {
        api(libs.androidx.core)
        api(libs.androidx.activity)
    }
    sourceSets.commonMain.dependencies {
        api(projects.common)
    }
}

android {
    namespace = "${Config.namespace}.system"
    configureAndroidLibrary(this)
}

publishMultiplatform()
