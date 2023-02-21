plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
}

android {
    namespace = "me.tylerbwong.adapter"
    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    api(libs.androidx.recyclerview)
    implementation(libs.androidx.core.ktx)
}
