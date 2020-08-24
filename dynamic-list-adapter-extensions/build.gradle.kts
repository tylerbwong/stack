import org.jetbrains.kotlin.android.synthetic.AndroidExtensionsFeature

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    StackPlugin
}

android {
    buildFeatures {
        buildConfig = false
    }
}

androidExtensions {
    features = setOf(AndroidExtensionsFeature.VIEWS.featureName)
}

dependencies {
    implementation(project(":dynamic-list-adapter"))
    implementation(Dep.kotlinLib)
    implementation(Dep.androidxRecyclerView)
}
