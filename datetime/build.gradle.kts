plugins {
    id("pro.respawn.shared-library")
}

android {
    namespace = "${Config.namespace}.datetime"
}

dependencies {
    commonMainApi(libs.kotlinx.datetime)
}
