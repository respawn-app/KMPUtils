@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("pro.respawn.shared-library")
    // id(libs.plugins.atomicfu.id)
    alias(libs.plugins.kotest)
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
