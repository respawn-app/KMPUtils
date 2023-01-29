plugins {
    id("shared-library")
}

kotlin {
    configureMultiplatform(
        this,
        android = false,
        ios = false, // TODO: unresolved :kotlin-native-prebuilt-macos-aarch64:1.8.0
        jvm = true,
    )
}

dependencies {
    commonMainApi(libs.kotlin.coroutines)
}