@file:Suppress(
    "MemberVisibilityCanBePrivate",
    "MissingPackageDeclaration",
    "UndocumentedPublicClass",
    "UndocumentedPublicProperty",
    "ConstPropertyName"
)

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object Config {

    const val group = "pro.respawn"
    const val artifact = "kmmutils"

    const val artifactId = "$group.$artifact"

    const val majorRelease = 1
    const val minorRelease = 6
    const val patch = 0
    const val postfix = ""
    const val versionName = "$majorRelease.$minorRelease.$patch$postfix"

    const val supportEmail = "hello@respawn.pro"
    const val vendorName = "Respawn Open Source Team"
    const val vendorId = "respawn-app"
    const val url = "https://github.com/respawn-app/kmputils"
    const val developerUrl = "https://respawn.pro"
    const val licenseName = "The Apache Software License, Version 2.0"
    const val licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt"
    const val scmUrl = "https://github.com/respawn-app/KMPUtils.git"
    const val description = """A collection of Kotlin Multiplatform essentials"""
    const val name = "KMPUtils"
    // kotlin

    const val compileSdk = 35
    const val targetSdk = compileSdk
    const val minSdk = 21
    const val appMinSdk = 26
    const val publishingVariant = "release"

    val jvmTarget = JvmTarget.JVM_11
    val javaVersion = JavaVersion.VERSION_11
    val optIns = listOf(
        "kotlin.RequiresOptIn",
        "kotlin.experimental.ExperimentalTypeInference",
        "kotlin.uuid.ExperimentalUuidApi",
        "kotlin.contracts.ExperimentalContracts",
    )
    val compilerArgs = listOf(
        "-Xexpect-actual-classes",
        "-Xconsistent-data-class-copy-visibility",
        "-Xwarning-level=NOTHING_TO_INLINE:disabled",
        "-Xwarning-level=UNUSED_ANONYMOUS_PARAMETER:disabled",
    )
    val jvmCompilerArgs = buildList {
        add("-Xjvm-default=all") // enable all jvm optimizations
        add("-Xcontext-parameters")
        add("-Xstring-concat=inline")
        add("-Xlambdas=indy")
        add("-Xjdk-release=${jvmTarget.target}")
    }

    val wasmCompilerArgs = buildList {
        add("-Xwasm-use-new-exception-proposal")
        add("-Xwasm-debugger-custom-formatters")
    }

    // android
    const val namespace = artifactId
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val isMinifyEnabledRelease = false
    const val isMinifyEnabledDebug = false
    const val defaultProguardFile = "proguard-android-optimize.txt"
    const val proguardFile = "proguard-rules.pro"
    const val consumerProguardFile = "consumer-rules.pro"

    // build scripts
    val stabilityLevels = listOf("preview", "eap", "dev", "alpha", "beta", "m", "cr", "rc")
    val minStabilityLevel = stabilityLevels.indexOf("beta")

    object Detekt {

        const val configFile = "detekt.yml"
        val includedFiles = listOf("**/*.kt", "**/*.kts")
        val excludedFiles = listOf("**/resources/**", "**/build/**", "**/.idea/**")
    }
}
