plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
}

android {
    buildFeatures {
        buildConfig = false
        viewBinding = true
    }
}

dependencies {
    api(projects.dynamicListAdapter)
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)
    implementation(libs.androidx.recyclerview)
}
