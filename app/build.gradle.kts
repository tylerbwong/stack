plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "me.tylerbwong.stack"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 6
        versionName = "0.2.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

androidExtensions {
    isExperimental = true
}

android {
    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // kotlin
    implementation(Dep.kotlinLib)
    implementation(Dep.kotlinCoroutinesCore)
    implementation(Dep.kotlinCoroutinesAndroid)

    // androidx
    implementation(Dep.androidxAppCompat)
    implementation(Dep.androidxBrowser)
    implementation(Dep.androidxLifecycleExtensions)
    implementation(Dep.androidxPreference)
    implementation(Dep.constraintLayout)
    implementation(Dep.ktxActivityExtensions)
    implementation(Dep.ktxViewModelExtensions)
    implementation(Dep.ktxLiveDataExtensions)
    implementation(Dep.materialComponents)

    // glide
    implementation(Dep.glide)
    kapt(Dep.glideProcessor)

    // networking
    implementation(Dep.gson)
    implementation(Dep.okHttpLogger)
    implementation(Dep.retrofit)
    implementation(Dep.retrofitGsonConverter)

    // markdown
    implementation(Dep.markwonCore)
    implementation(Dep.markwonImageOkhttp)
    implementation(Dep.markwonStrikethrough)

    // logging
    implementation(Dep.timber)

    // room
    implementation(Dep.room)
    implementation(Dep.roomCoroutines)
    kapt(Dep.roomProcessor)

    // testing
    testImplementation(Dep.androidxTestCore)
    testImplementation(Dep.jUnit)
    testImplementation(Dep.robolectric)
    androidTestImplementation(Dep.testRunner)
    androidTestImplementation(Dep.espresso)
}
