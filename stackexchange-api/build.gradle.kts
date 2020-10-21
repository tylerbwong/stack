plugins {
    id("com.android.library")
    `kotlin-android`
    `kotlin-kapt`
    StackPlugin
}

android {
    defaultConfig {
        buildConfigField("String", "API_KEY", stringProperty("stackApiKey"))
    }
}

stack {
    isMetalavaEnabled = true
}

dependencies {
    implementation(Dep.kotlinLib)

    // dagger
    implementation(Dep.daggerHiltAndroid)
    kapt(Dep.daggerHiltAndroidCompiler)

    // networking
    implementation(Dep.moshi)
    kapt(Dep.moshiKotlinCodegen)
    implementation(Dep.okHttp)
    implementation(Dep.retrofit)
    implementation(Dep.retrofitMoshiConverter)
}
