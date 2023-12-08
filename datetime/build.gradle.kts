plugins {
    id("pro.respawn.shared-library")
}

kotlin {
    configureMultiplatform(this, android = false)
}

dependencies {
    commonMainApi(libs.kotlinx.datetime)
}
