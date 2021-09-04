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
    api(projects.dynamicListAdapter)
    api(libs.androidx.recyclerview)
}
