plugins {
    id("com.android.application")
    `kotlin-android`
    StackPlugin
}

android {
    namespace = "me.tylerbwong.compose.markdown.demo"
    buildFeatures {
        compose = true
        buildConfig = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }
}

dependencies {
    // androidx
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    // compose
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)

    // markdown
    implementation(projects.composeMarkdown)
}
