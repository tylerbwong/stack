plugins {
    id("com.android.library")
    `kotlin-android`
    `kotlin-kapt`
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
}

android {
    defaultConfig {
        buildConfigField("String", "API_VERSION", "\"2.3\"")
        buildConfigField("String", "API_KEY", stringProperty("stackApiKey"))
    }
}

dependencies {
    // dagger
    kapt(libs.google.dagger.hilt.android.compiler)
    implementation(libs.google.dagger.hilt.android)

    // networking
    implementation(libs.moshi)
    kapt(libs.moshi.kotlinCodegen)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
}
