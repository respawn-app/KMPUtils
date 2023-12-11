plugins {
    id("pro.respawn.shared-library")
}

kotlin {
    configureMultiplatform(this, android = false)
    sourceSets.jvmTest.dependencies {
        implementation(libs.bundles.unittest)
    }
}

dependencies {
    commonMainApi(projects.common)
}
