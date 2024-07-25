plugins {
    id("pro.respawn.shared-library")
    alias(libs.plugins.maven.publish)
}
dependencies {
    commonMainApi(projects.common)
    jvmTestImplementation(libs.bundles.unittest)
}
