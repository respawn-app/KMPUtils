@file:Suppress("Unused")

plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

val libs by versionCatalog

configurePublication()
