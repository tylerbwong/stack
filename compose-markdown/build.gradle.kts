plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
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
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)

    // androidx
    implementation(libs.androidx.core.ktx)

    // coil
    implementation(libs.coil.compose)

    // compose
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.material.material)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.ui)

    // markdown
    implementation(libs.jetbrains.markdown)

    // testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
}
