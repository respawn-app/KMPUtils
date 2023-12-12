plugins {
    id("pro.respawn.shared-library")
    id(libs.plugins.atomicfu.id)
}
android {
    namespace = "${Config.namespace}.coroutines"
}
dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonMainImplementation(libs.kotlinx.atomicfu)
}
