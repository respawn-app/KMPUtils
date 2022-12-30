@file:Suppress("MissingPackageDeclaration")

import org.gradle.api.Project
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
fun Project.configureMultiplatform(
    ext: KotlinMultiplatformExtension,
    android: Boolean = false,
    ios: Boolean = false,
    jvm: Boolean = false,
) = ext.apply {

    if (android) {
        android()
    }

    if (jvm) {
        jvm {
            compilations.all {
                kotlinOptions.jvmTarget = Config.jvmTarget
            }
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
            }
        }
    }

    if (ios) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                binaryOption("bundleId", Config.applicationId)
                binaryOption("bundleVersion", Config.versionName)
                baseName = Config.applicationId
            }
        }

        sourceSets.apply {
            all {
                languageSettings.apply {
                    optIn("kotlin.RequiresOptIn")
                    progressiveMode = true
                }
            }

            val commonMain by getting
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
            val androidMain by getting
            val androidTest by getting
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
            }
            val iosX64Test by getting
            val iosArm64Test by getting
            val iosSimulatorArm64Test by getting
            val iosTest by creating {
                dependsOn(commonTest)
                iosX64Test.dependsOn(this)
                iosArm64Test.dependsOn(this)
                iosSimulatorArm64Test.dependsOn(this)
            }
        }
    }
}
