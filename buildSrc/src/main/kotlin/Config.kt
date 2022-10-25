@file:Suppress("MemberVisibilityCanBePrivate", "MissingPackageDeclaration")

import org.gradle.api.JavaVersion

object Config  {

    const val group = "com.nek12"
    const val artifact = "kmmutils"

    const val applicationId = "$group.$artifact"
    const val versionCode = 1

    const val majorRelease = 0
    const val minorRelease = 1
    const val patch = 0
    const val versionName = "$majorRelease.$minorRelease.$patch"

    val javaVersion = JavaVersion.VERSION_11
    const val jvmTarget = "11"
    const val compileSdk = 33
    const val targetSdk = compileSdk
    const val minSdk = 26

    const val isMinifyEnabledDebug = false
    const val isMinifyEnabledRelease = false

    const val defaultProguardFile = "proguard-android-optimize.txt"
    const val proguardFile = "proguard-rules.pro"
    const val consumerProguardFile = "consumer-rules.pro"

    val supportedLocales = listOf("en")
    val kotlinCompilerArgs = listOf(
        "-Xjvm-default=all", // enable all jvm optimizations
        "-Xcontext-receivers",
        "-Xbackend-threads=0", // parallel IR compilation
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.FlowPreview",
        "-opt-in=kotlin.Experimental",
        "-opt-in=kotlin.RequiresOptIn",
        // "-Xuse-k2",
        // "-XXLanguage:+ExplicitBackingFields"
    )

    object Detekt {

        const val configFile = "detekt.yml"
        val includedFiles = listOf("**/*.kt") //  "**/*.kts"
        val excludedFiles = listOf("**/resources/**", "**/build/**", "**/.idea/**")
    }
}
