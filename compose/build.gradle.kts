import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id(libs.plugins.kotlin.multiplatform.id)
    id(libs.plugins.androidLibrary.id)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
    dokkaDocumentation
}

android {
    configureAndroidLibrary(this)
    namespace = namespaceByPath()

    buildFeatures {
        compose = true
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    configureMultiplatform(
        ext = this,
        jvm = true,
        android = true,
        iOs = true,
        macOs = true,
        watchOs = false,
        tvOs = false,
        linux = false,
        js = true,
        wasmJs = true,
        windows = false,
        wasmWasi = false,
    ) {
        common {
            group("web") {
                withJs()
                withWasmJs()
            }
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(compose.components.resources)

            api(libs.lifecycle.runtime)
            api(libs.lifecycle.compose)
            implementation(libs.compose.window.size)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.animationGraphics)
            implementation(compose.animation)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.common)
        }
        androidMain.dependencies {
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.activity.compose)
            implementation(projects.system)
        }
    }
}
