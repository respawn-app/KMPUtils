@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("pro.respawn.shared-library")
}

kotlin {
    configureMultiplatform(
        this,
        android = false,
        ios = true,
        jvm = true,
        js = true,
        linux = true,
        mingw = true,
    )
}

dependencies {
    commonMainApi(project(":common"))
}
