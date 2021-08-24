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
    api(libs.androidx.recyclerview)
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)
}
