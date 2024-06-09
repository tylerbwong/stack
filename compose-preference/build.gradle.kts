plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "me.tylerbwong.compose.preference"
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
    implementation(libs.androidx.activity.ktx)

    // compose
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.runner)
}
