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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        google()
        ivyNative()
        mavenCentral()
    }
}

rootProject.name = "kmmutils"

include(":apiresult")
include(":common")
include(":datetime")
include(":coroutines")


/**
 *  workaround CI pipeline resolution bug
 */
fun RepositoryHandler.ivyNative() {
    ivy { url = java.net.URI("https://download.jetbrains.com") }

    exclusiveContent {
        forRepository {
            this@ivyNative.ivy("https://download.jetbrains.com/kotlin/native/builds") {
                name = "Kotlin Native"
                patternLayout {

                    listOf(
                        "macos-x86_64",
                        "macos-aarch64",
                        "osx-x86_64",
                        "osx-aarch64",
                        "linux-x86_64",
                        "windows-x86_64",
                    ).forEach { os ->
                        listOf("dev", "releases").forEach { stage ->
                            artifact("$stage/[revision]/$os/[artifact]-[revision].[ext]")
                        }
                    }
                }
                metadataSources { artifact() }
            }
        }
        filter { includeModuleByRegex(".*", ".*kotlin-native-prebuilt.*") }
    }
}
