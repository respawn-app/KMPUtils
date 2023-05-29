import nl.littlerobots.vcu.plugin.versionCatalogUpdate

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.gradleDoctor)
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencyAnalysis)
    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
}

buildscript {
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.version.gradle)
        classpath(libs.detekt.gradle)
    }
}

allprojects {
    group = Config.artifactId
    version = Config.versionName
}

subprojects {
    apply(plugin = rootProject.libs.plugins.dokka.id)

    dependencies {
        dokkaPlugin(rootProject.libs.dokka.android)
    }

    tasks {
        register<org.gradle.jvm.tasks.Jar>("dokkaJavadocJar") {
            // TODO: Dokka does not support javadocs for multiplatform dependencies
            // dependsOn(dokkaJavadoc)
            // from(dokkaJavadoc.flatMap { it.outputDirectory })
            archiveClassifier.set("javadoc")
        }
    }
}

doctor {
    javaHome {
        ensureJavaHomeMatches.set(false)
    }
}

dependencyAnalysis {
    issues {
        all {
            ignoreKtx(true)
        }
    }
}

dependencies {
    detektPlugins(rootProject.libs.detekt.formatting)
    detektPlugins(rootProject.libs.detekt.compose)
    detektPlugins(rootProject.libs.detekt.libraries)
}

versionCatalogUpdate {
    sortByKey.set(true)

    keep {
        keepUnusedVersions.set(true)
        keepUnusedLibraries.set(true)
        keepUnusedPlugins.set(true)
    }
}

tasks {
    dokkaHtmlMultiModule.configure {
        moduleName.set(rootProject.name)
        outputDirectory.set(buildDir.resolve("dokka"))
    }
    // needed to generate compose compiler reports. See /scripts
    withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        buildUponDefaultConfig = true
        parallel = true
        setSource(projectDir)
        config.setFrom(File(rootDir, Config.Detekt.configFile))
        basePath = projectDir.absolutePath
        jvmTarget = Config.jvmTarget.target
        include(Config.Detekt.includedFiles)
        exclude(Config.Detekt.excludedFiles)
        reports {
            xml.required.set(false)
            html.required.set(true)
            txt.required.set(false)
            sarif.required.set(true)
            md.required.set(false)
        }
    }

    register<io.gitlab.arturbosch.detekt.Detekt>("detektFormat") {
        description = "Formats whole project."
        autoCorrect = true
    }

    register<io.gitlab.arturbosch.detekt.Detekt>("detektAll") {
        description = "Run detekt on whole project"
        autoCorrect = false
    }

    withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>().configureEach {
        outputFormatter = "json"

        fun stabilityLevel(version: String): Int {
            Config.stabilityLevels.forEachIndexed { index, postfix ->
                val regex = """.*[.\-]$postfix[.\-\d]*""".toRegex(RegexOption.IGNORE_CASE)
                if (version.matches(regex)) return index
            }
            return Config.stabilityLevels.size
        }

        rejectVersionIf {
            stabilityLevel(currentVersion) > stabilityLevel(candidate.version)
        }
    }
}

// TODO: https://github.com/Kotlin/dokka/issues/2977
val taskClass = "org.jetbrains.kotlin.gradle.targets.native.internal.CInteropMetadataDependencyTransformationTask"
gradle.taskGraph.whenReady {
    val hasDokkaTasks = gradle.taskGraph.allTasks.any { it is org.jetbrains.dokka.gradle.AbstractDokkaTask }
    if (hasDokkaTasks) {
        @Suppress("UNCHECKED_CAST")
        tasks.withType(Class.forName(taskClass) as Class<Task>).configureEach {
            enabled = false
        }
    }
}
