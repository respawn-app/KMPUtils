plugins {
    id("shared-library")
}

kotlin {
    configureMultiplatform(
        this,
        android = false,
        ios = false,
        jvm = true,
    )
}

dependencies {
    commonMainApi(libs.kotlin.coroutines)
}
