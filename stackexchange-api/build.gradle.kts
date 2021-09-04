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
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.dagger.hilt.android.core)

    // networking
    implementation(libs.moshi.core)
    kapt(libs.moshi.kotlin.codegen)
    implementation(libs.okhttp.core)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
}
