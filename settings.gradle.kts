@file:Suppress("UndocumentedPublicFunction")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    // kmm plugin adds "ivy" repo as part of the apply block
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmmutils"

include(":common")
include(":datetime")
include(":coroutines")
include(":inputforms")
include(":compose")
include(":system")
