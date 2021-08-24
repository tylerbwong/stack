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
    api(libs.androidx.recyclerview)
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)
    implementation(libs.androidx.core.ktx)
}
