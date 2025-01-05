plugins {
    id("pro.respawn.shared-library")
    alias(libs.plugins.atomicfu)
    alias(libs.plugins.maven.publish)
    dokkaDocumentation
}
dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonMainImplementation(libs.kotlinx.atomicfu)
}
