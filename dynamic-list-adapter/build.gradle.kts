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
    implementation(Dep.kotlinLib)
    implementation(Dep.androidxCore)
    implementation(Dep.androidxRecyclerView)
}
