@file:Suppress("Unused")

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
}

val libs by versionCatalog

configurePublication()

kotlin {
    configureMultiplatform(
        this,
        android = true,
        ios = false, // TODO: unresolved :kotlin-native-prebuilt-macos-aarch64:1.8.0
        jvm = true,
    )
}

android {
    configureAndroidLibrary(this)
}
