plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
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
