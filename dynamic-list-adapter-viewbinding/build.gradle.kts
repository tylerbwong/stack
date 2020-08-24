plugins {
    id("com.android.library")
    kotlin("android")
    StackPlugin
}

android {
    buildFeatures {
        buildConfig = false
        viewBinding = true
    }
}

dependencies {
    implementation(project(":dynamic-list-adapter"))
    implementation(Dep.kotlinLib)
    implementation(Dep.androidxRecyclerView)
}
