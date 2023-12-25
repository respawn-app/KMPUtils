plugins {
    kotlin("multiplatform")
    // id("com.android.library")
    id("maven-publish")
    signing
}

kotlin {
    configureMultiplatform(this)
}

// android {
//     configureAndroidLibrary(this)
// }

publishMultiplatform()
