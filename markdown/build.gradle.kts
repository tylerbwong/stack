plugins {
    id("com.android.library")
    `kotlin-android`
    `kotlin-kapt`
    id("dagger.hilt.android.plugin")
    StackPlugin
}

android {
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
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.dagger.hilt.android.core)

    // markdown
    implementation(libs.apache.commons.text)
    implementation(libs.markwon.core)
    implementation(libs.markwon.syntaxhighlight)
    kapt(libs.prism4j.bundler)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric)
}
