@file:Suppress(
    "MemberVisibilityCanBePrivate",
    "MissingPackageDeclaration",
    "UndocumentedPublicProperty",
    "UndocumentedPublicFunction"
)

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.ivy
import org.gradle.plugin.use.PluginDependency
import java.net.URI
import java.util.Base64

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

// fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinMultiplatformCommonOptions.() -> Unit) {
//     (this as ExtensionAware).extensions.configure("kotlinOptions", block)
// }

/**
 * Creates a java array initializer code for a list of strings.
 * { "a", "b", "c" }
 */
fun List<String>.toJavaArrayString() = buildString {
    append("{")

    this@toJavaArrayString.forEachIndexed { i, it ->

        append("\"$it\"")

        if (i != this@toJavaArrayString.lastIndex) {
            append(", ")
        }
    }

    append("}")
}

fun String.toBase64() = Base64.getEncoder().encodeToString(toByteArray())

/**
 *  workaround CI pipeline resolution bug
 */
fun RepositoryHandler.ivy() {
    ivy { url = URI("https://download.jetbrains.com") }

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
