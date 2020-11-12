plugins {
    id("com.android.library")
    `kotlin-android`
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
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

    // androidx
    implementation(Dep.androidxCore)

    // coil
    implementation(Dep.coilAccompanist)

    // compose
    implementation(Dep.composeCore)
    implementation(Dep.composeFoundation)
    implementation(Dep.composeMaterial)
    implementation(Dep.composeText)
    implementation(Dep.composeTooling)

    // markdown
    implementation(Dep.intellijMarkdown)

    // testing
    testImplementation(Dep.jUnit)
    testImplementation(Dep.mockito)
    testImplementation(Dep.mockitoKotlin)
}
