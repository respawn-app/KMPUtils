import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import com.vanniktech.maven.publish.SonatypeHost
import nl.littlerobots.vcu.plugin.versionCatalogUpdate
import nl.littlerobots.vcu.plugin.versionSelector
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.gradleDoctor)
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencyAnalysis)
    alias(libs.plugins.atomicfu)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.maven.publish) apply false
    // plugins already on a classpath (conventions)
    // alias(libs.plugins.androidApplication) apply false
    // alias(libs.plugins.androidLibrary) apply false
    // alias(libs.plugins.kotlinMultiplatform) apply false
}

buildscript {
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.detekt.gradle)
    }
}

allprojects {
    group = Config.artifactId
    version = Config.versionName
    plugins.withType<ComposeCompilerGradleSubplugin>().configureEach {
        the<ComposeCompilerGradlePluginExtension>().apply {
            enableIntrinsicRemember = true
            enableNonSkippingGroupOptimization = true
            enableStrongSkippingMode = true
            stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_definitions.txt")
            if (properties["enableComposeCompilerReports"] == "true") {
                val metricsDir = layout.buildDirectory.dir("compose_metrics")
                metricsDestination = metricsDir
                reportsDestination = metricsDir
            }
        }
    }
    plugins.withType<MavenPublishPlugin>().configureEach {
        the<MavenPublishBaseExtension>().apply {
            val isReleaseBuild = properties["release"]?.toString().toBoolean()
            publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, false)
            if (isReleaseBuild) signAllPublications()
            coordinates(Config.artifactId, name, Config.version(isReleaseBuild))
            pom {
                name = Config.name
                description = Config.description
                url = Config.url
                licenses {
                    license {
                        name = Config.licenseName
                        url = Config.licenseUrl
                        distribution = Config.licenseUrl
                    }
                }
                developers {
                    developer {
                        id = Config.vendorId
                        name = Config.vendorName
                        url = Config.developerUrl
                        email = Config.supportEmail
                        organizationUrl = Config.developerUrl
                    }
                }
                scm {
                    url = Config.scmUrl
                }
            }
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(Config.jvmTarget)
            freeCompilerArgs.addAll(Config.jvmCompilerArgs)
            optIn.addAll(Config.optIns)
        }
    }
}

atomicfu {
    dependenciesVersion = rootProject.libs.versions.kotlinx.atomicfu.get()
    transformJvm = false
    jvmVariant = "VH"
    transformJs = false
}

subprojects {
    apply(plugin = rootProject.libs.plugins.dokka.id)

    dependencies {
        dokkaPlugin(rootProject.libs.dokka.android)
    }

    tasks {
        withType<Test>().configureEach {
            useJUnitPlatform()
            filter { isFailOnNoMatchingTests = true }
        }
        register<org.gradle.jvm.tasks.Jar>("dokkaJavadocJar") {
            dependsOn(dokkaJavadoc)
            from(dokkaJavadoc.flatMap { it.outputDirectory })
            archiveClassifier.set("javadoc")
        }

        register<org.gradle.jvm.tasks.Jar>("emptyJavadocJar") {
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
    structure {
        ignoreKtx(true)
    }
}

dependencies {
    detektPlugins(rootProject.libs.detekt.formatting)
    detektPlugins(rootProject.libs.detekt.compose)
    detektPlugins(rootProject.libs.detekt.libraries)
}

versionCatalogUpdate {
    sortByKey = true

    versionSelector { stabilityLevel(it.candidate.version) >= Config.minStabilityLevel }

    keep {
        keepUnusedVersions = true
        keepUnusedLibraries = true
        keepUnusedPlugins = true
    }
}

tasks {
    dokkaHtmlMultiModule.configure {
        moduleName.set(rootProject.name)
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

    wrapper {
        distributionType = Wrapper.DistributionType.BIN
    }
}
rootProject.plugins.withType<YarnPlugin>().configureEach {
    rootProject.the<YarnRootExtension>().apply {
        yarnLockMismatchReport = YarnLockMismatchReport.WARNING // NONE | FAIL | FAIL_AFTER_BUILD
        reportNewYarnLock = true
        yarnLockAutoReplace = true
    }
}
