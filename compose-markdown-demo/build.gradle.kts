plugins {
    id("com.android.application")
    `kotlin-android`
    StackPlugin
}

android {
    buildFeatures {
        compose = true
        buildConfig = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    // kotlin
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)

    // androidx
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    // compose
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.ui)

    // markdown
    implementation(projects.composeMarkdown)
}
