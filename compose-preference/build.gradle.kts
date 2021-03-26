plugins {
    id("com.android.library")
    `kotlin-android`
    id("com.facebook.testing.screenshot")
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
}

android {
    defaultConfig {
        testInstrumentationRunner = "me.tylerbwong.compose.preference.PreferenceTestRunner"
    }

    buildFeatures {
        compose = true
        buildConfig = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(Dep.androidxActivity)
    implementation(Dep.flowSharedPreferences)
    implementation(Dep.kotlinLib)

    // compose
    implementation(Dep.composeFoundation)
    implementation(Dep.composeMaterial)
    implementation(Dep.composeMaterialIcons)
    implementation(Dep.composeMaterialIconsExtended)
    implementation(Dep.composeTooling)
    implementation(Dep.composeUi)

    androidTestImplementation(Dep.androidxTestCore)
    androidTestImplementation(Dep.androidxTestExt)
    androidTestImplementation(Dep.androidxTestRunner)
    androidTestImplementation(Dep.composeUiTest)
}
