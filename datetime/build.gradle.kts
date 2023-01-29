plugins {
    id("shared-library")
}

kotlin {
    configureMultiplatform(
        this,
        android = false,
        ios = true,
        jvm = true,
    )
}

dependencies {
    commonMainApi(libs.kotlin.datetime)
}
