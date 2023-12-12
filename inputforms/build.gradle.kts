plugins {
    id("pro.respawn.shared-library")
}
android {
    namespace = "${Config.namespace}.inputforms"
}
dependencies {
    commonMainApi(projects.common)
    jvmTestImplementation(libs.bundles.unittest)
}
