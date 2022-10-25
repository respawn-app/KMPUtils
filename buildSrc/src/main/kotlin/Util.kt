@file:Suppress("MemberVisibilityCanBePrivate", "MissingPackageDeclaration")

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File
import java.io.IOException
import java.util.Optional
import java.util.concurrent.TimeUnit

fun generateGitPatchVersion(): Int = "git rev-list HEAD --count".runCommand().trim().toInt()

fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 10,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
    .directory(workingDir)
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .redirectError(ProcessBuilder.Redirect.PIPE)
    .start()
    .apply { waitFor(timeoutAmount, timeoutUnit) }
    .run {
        val error = errorStream.bufferedReader().readText().trim()
        if (error.isNotEmpty()) {
            throw IOException(error)
        }
        inputStream.bufferedReader().readText().trim()
    }

/**
 * Load version catalog for usage in places where it is not available yet with gradle 7.x.
 * to obtain a version/lib use:
 * ```
 * val libs by versionCatalog
 * libs.findVersion("androidxCompose").get().toString()
 * libs.requireLib("androidx.core.ktx")
 * ```
 */
val Project.versionCatalog: Lazy<VersionCatalog>
    get() = lazy {
        extensions.getByType<VersionCatalogsExtension>().named("libs")
    }

fun VersionCatalog.requirePlugin(alias: String) = findPlugin(alias).get().toString()
fun VersionCatalog.requireLib(alias: String) = findLibrary(alias).get()
fun VersionCatalog.requireBundle(alias: String) = findBundle(alias).get()

val org.gradle.api.provider.Provider<PluginDependency>.id: String get() = get().pluginId

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

val Project.isReleaseBuild: Boolean
    get() = properties.containsKey("signingKey")
