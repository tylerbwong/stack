plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    StackPlugin
}

android {
    buildFeatures {
        buildConfig = false
    }
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
