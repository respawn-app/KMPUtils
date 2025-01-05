plugins {
    id("pro.respawn.shared-library")
    alias(libs.plugins.maven.publish)
    dokkaDocumentation
}

dependencies {
    commonMainApi(libs.kotlinx.datetime)
}
