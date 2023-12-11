@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("pro.respawn.shared-library")
}

kotlin {
    configureMultiplatform(this, android = false)
}
