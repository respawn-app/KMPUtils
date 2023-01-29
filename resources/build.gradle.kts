import Config.namespace

plugins {
    id("android-library")
    id("shared-library")
}

kotlin {
    configureMultiplatform(
        this,
        android = true,
        ios = true,
        jvm = true,
    )
}

android {
    namespace = "${Config.namespace}.resources"
}
