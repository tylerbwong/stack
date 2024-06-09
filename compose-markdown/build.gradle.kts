plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "me.tylerbwong.compose.markdown"
    buildFeatures {
        compose = true
        buildConfig = false
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}

metalava {
    sourcePaths.setFrom("src/main")
}

dependencies {
    // androidx
    implementation(libs.androidx.core.ktx)

    // coil
    implementation(libs.coil.compose)

    // compose
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)

    // markdown
    implementation(libs.jetbrains.markdown)

    // testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
}
