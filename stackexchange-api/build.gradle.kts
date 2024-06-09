plugins {
    id("com.android.library")
    `kotlin-android`
    kotlin("plugin.serialization") version libs.versions.jetbrains.kotlin
    alias(libs.plugins.google.ksp)
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
}

android {
    namespace = "me.tylerbwong.stack.api"
    defaultConfig {
        buildConfigField("String", "API_VERSION", "\"2.3\"")
        buildConfigField("String", "API_KEY", stringProperty("stackApiKey"))
    }
    buildFeatures {
        buildConfig = true
    }
}

metalava {
    sourcePaths.setFrom("src/main")
}

dependencies {
    // dagger
    ksp(libs.google.dagger.hilt.android.compiler)
    implementation(libs.google.dagger.hilt.android)

    // networking
    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
}
