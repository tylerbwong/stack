plugins {
    id("com.android.application")
    `kotlin-android`
    StackPlugin
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Versions.kotlin
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    // kotlin
    implementation(Dep.kotlinLib)

    // androidx
    implementation(Dep.androidxAppCompat)
    implementation(Dep.materialComponents)

    // compose
    implementation(Dep.composeCore)
    implementation(Dep.composeFoundation)
    implementation(Dep.composeTooling)

    // markdown
    implementation(markdownCompose())
}
