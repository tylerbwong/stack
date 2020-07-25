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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":dynamic-list-adapter"))
    implementation(Dep.kotlinLib)
    implementation(Dep.androidxRecyclerView)
}
