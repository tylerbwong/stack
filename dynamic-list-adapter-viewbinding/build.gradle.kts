plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
}

android {
    namespace = "me.tylerbwong.adapter.viewbinding"
    buildFeatures {
        buildConfig = false
        viewBinding = true
    }
}

dependencies {
    api(projects.dynamicListAdapter)
    api(libs.androidx.recyclerview)
}
