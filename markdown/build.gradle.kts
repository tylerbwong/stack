plugins {
    id("com.android.library")
    `kotlin-android`
    `kotlin-kapt`
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.dagger.hilt)
    StackPlugin
}

android {
    namespace = "me.tylerbwong.stack.markdown"
    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    // androidx
    implementation(libs.androidx.appcompat)

    // betterlinkmovementmethod
    implementation(libs.betterlinkmovementmethod)

    // dagger
    ksp(libs.google.dagger.hilt.android.compiler)
    implementation(libs.google.dagger.hilt.android)

    // markdown
    implementation(libs.apache.commonsText)
    implementation(libs.markwon.core)
    implementation(libs.markwon.syntaxhighlight)
    kapt(libs.prism4jBundler)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric)
}
