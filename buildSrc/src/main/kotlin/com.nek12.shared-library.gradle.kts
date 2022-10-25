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
        ios = true,
        jvm = true,
    )
}

android {
    configureAndroidLibrary(this)

}
