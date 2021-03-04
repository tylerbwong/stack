plugins {
    id("com.android.application")
    `kotlin-android`
    StackPlugin
}

android {
    buildFeatures {
        compose = true
        buildConfig = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    // kotlin
    implementation(Dep.kotlinLib)

    // androidx
    implementation(Dep.androidxActivityCompose)
    implementation(Dep.androidxAppCompat)
    implementation(Dep.materialComponents)

    // compose
    implementation(Dep.composeCore)
    implementation(Dep.composeFoundation)
    implementation(Dep.composeTooling)

    // markdown
    implementation(composeMarkdown())
}
