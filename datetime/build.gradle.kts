plugins {
    id("pro.respawn.shared-library")
    alias(libs.plugins.maven.publish)
}

dependencies {
    commonMainApi(libs.kotlinx.datetime)
}
