plugins {
    id("com.android.library")
    `kotlin-android`
    `kotlin-kapt`
    alias(libs.plugins.google.ksp)
    StackPlugin
    id("me.tylerbwong.gradle.metalava")
}

android {
    namespace = "me.tylerbwong.stack.api"
    defaultConfig {
        buildConfigField("String", "API_VERSION", "\"2.3\"")
        buildConfigField("String", "API_KEY", stringProperty("stackApiKey"))
    }
}

metalava {
    sourcePaths = mutableSetOf("src/main")
}

dependencies {
    // dagger
    kapt(libs.google.dagger.hilt.android.compiler)
    implementation(libs.google.dagger.hilt.android)

    // networking
    implementation(libs.moshi)
    ksp(libs.moshi.kotlinCodegen)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
}
