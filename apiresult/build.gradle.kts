plugins {
    id("pro.respawn.shared-library")
}


kotlin {
    configureMultiplatform(
        this,
        android = false,
        ios = true,
        jvm = true,
        js = true,
        linux = true,
        mingw = true,
    )

    sourceSets.apply {
        val jvmTest by getting {
            dependencies {
                implementation(libs.bundles.unittest)
            }
        }
    }
}


dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
}
