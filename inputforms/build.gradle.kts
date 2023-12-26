plugins {
    id("pro.respawn.shared-library")
}
dependencies {
    commonMainApi(projects.common)
    jvmTestImplementation(libs.bundles.unittest)
}
