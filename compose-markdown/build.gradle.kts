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
    implementation(Dep.composeFoundation)
    implementation(Dep.composeMaterial)
    implementation(Dep.composeTooling)
    implementation(Dep.composeUi)

    // markdown
    implementation(Dep.intellijMarkdown)

    // testing
    testImplementation(Dep.jUnit)
    testImplementation(Dep.mockito)
    testImplementation(Dep.mockitoKotlin)
}
