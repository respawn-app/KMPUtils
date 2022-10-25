plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.gradleDoctor)
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.dependencyAnalysis)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.version.gradle)
        classpath(libs.detekt.gradle)
    }
}

allprojects {
    group = "com.nek12"
    version = Config.versionName

    tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }
}

doctor {
    javaHome {
        ensureJavaHomeMatches.set(false)
    }
}

versionCatalogUpdate {
    sortByKey.set(true)

    keep {
        keepUnusedVersions.set(true)
        keepUnusedLibraries.set(true)
        keepUnusedPlugins.set(true)
    }
}
