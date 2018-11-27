import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "me.tylerbwong.stack"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 5
        versionName = "0.2.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

androidExtensions {
    // https://youtrack.jetbrains.com/issue/KT-22213
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

android {
    lintOptions {
        setAbortOnError(false)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // kotlin
    implementation(Dep.kotlinLib)
    implementation(Dep.kotlinCoroutinesCore)
    implementation(Dep.kotlinCoroutinesAndroid)
    implementation(Dep.kotlinCoroutinesRetrofit)
    implementation(Dep.kotlinCoroutinesRx)

    // androidx
    implementation(Dep.androidxAppCompat)
    implementation(Dep.androidxBrowser)
    implementation(Dep.androidxCardView)
    implementation(Dep.androidxExtensions)
    implementation(Dep.constraintLayout)
    implementation(Dep.materialComponents)

    // glide
    implementation(Dep.glide)
    kapt(Dep.glideProcessor)

    // networking
    implementation(Dep.gson)
    implementation(Dep.okHttpLogger)
    implementation(Dep.retrofit)
    implementation(Dep.retrofitRxAdapter)
    implementation(Dep.retrofitGsonConverter)
    implementation(Dep.stetho)
    implementation(Dep.stethoOkHttp)

    // rx
    implementation(Dep.rxAndroid)
    implementation(Dep.rxJava)

    // markdown
    implementation(Dep.markwon)
    implementation(Dep.markwonImageLoader)

    // logging
    implementation(Dep.timber)

    // room
    implementation(Dep.room)
    implementation(Dep.roomRxSupport)
    kapt(Dep.roomProcessor)

    // testing
    testImplementation(Dep.jUnit)
    androidTestImplementation(Dep.testRunner)
    androidTestImplementation(Dep.espresso)
}
