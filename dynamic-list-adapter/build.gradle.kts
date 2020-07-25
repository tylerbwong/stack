plugins {
    id("com.android.library")
    kotlin("android")
    StackPlugin
}

android {
    buildFeatures {
        buildConfig = false
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Dep.kotlinLib)
    implementation(Dep.androidxCore)
    implementation(Dep.androidxRecyclerView)
}
