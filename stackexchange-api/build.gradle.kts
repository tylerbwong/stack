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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
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
