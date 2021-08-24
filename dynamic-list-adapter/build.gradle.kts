plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
}

android {
    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.recyclerview)
}
