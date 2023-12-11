plugins {
    id("pro.respawn.shared-library")
    id(libs.plugins.atomicfu.id)
}

kotlin {
    configureMultiplatform(this, android = false)
}

dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonMainImplementation(libs.kotlinx.atomicfu)
}
