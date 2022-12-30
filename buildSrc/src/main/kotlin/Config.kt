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
    const val versionName = "$majorRelease.$minorRelease.$patch-alpha"

    val kotlinCompilerArgs = listOf(
        "-Xjvm-default=all", // enable all jvm optimizations
        "-Xcontext-receivers",
        "-Xbackend-threads=0", // parallel IR compilation
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.FlowPreview",
        "-opt-in=kotlin.Experimental",
        "-opt-in=kotlin.RequiresOptIn",
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        // "-Xuse-k2",
        // "-XXLanguage:+ExplicitBackingFields"
    )

    val javaVersion = JavaVersion.VERSION_11
    const val jvmTarget = "11"
    const val compileSdk = 33
    const val targetSdk = compileSdk
    const val minSdk = 26
    const val kotlinVersion = "1.8"

    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val isMinifyEnabledRelease = true
    const val isMinifyEnabledDebug = false
    const val defaultProguardFile = "proguard-android-optimize.txt"
    const val proguardFile = "proguard-rules.pro"
    const val consumerProguardFile = "consumer-rules.pro"

    object Detekt {

        const val configFile = "detekt.yml"
        val includedFiles = listOf("**/*.kt", "**/*.kts")
        val excludedFiles = listOf("**/resources/**", "**/build/**", "**/.idea/**")
    }
}
