import Config.compileSdk

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

android {
    configureAndroidLibrary(this)
}
