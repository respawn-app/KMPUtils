plugins {
    kotlin("android")
    id("com.android.library")
}

kotlin {
    compilerOptions {
        jvmTarget.set(Config.jvmTarget)
    }
    explicitApi()
}

android {
    configureAndroidLibrary(this)
}
