plugins {
    id("pro.respawn.shared-library")
    id(libs.plugins.atomicfu.id)
}
dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonMainImplementation(libs.kotlinx.atomicfu)
}
