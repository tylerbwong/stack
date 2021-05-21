plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
}

android {
    buildFeatures {
        buildConfig = false
        viewBinding = true
    }
}

dependencies {
    implementation(projects.dynamicListAdapter)
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)
    implementation(libs.androidx.recyclerview)
}
