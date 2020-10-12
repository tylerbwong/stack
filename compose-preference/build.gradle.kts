plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
}

android {
    buildFeatures {
        compose = true
        buildConfig = false
    }

    composeOptions {
        kotlinCompilerVersion = Versions.kotlin
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(Dep.kotlinLib)

    // compose
    implementation(Dep.composeCore)
    implementation(Dep.composeFoundation)
    implementation(Dep.composeMaterial)
    implementation(Dep.composeMaterialIcons)
    implementation(Dep.composeMaterialIconsExtended)
    implementation(Dep.composeText)
    implementation(Dep.composeTooling)
}
