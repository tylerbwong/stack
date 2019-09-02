plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("io.fabric")
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

    lintOptions {
        isAbortOnError = false
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

androidExtensions {
    isExperimental = true
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
    implementation(Dep.androidxSwipeRefreshLayout)
    implementation(Dep.constraintLayout)
    implementation(Dep.ktxActivityExtensions)
    implementation(Dep.ktxFragmentExtensions)
    implementation(Dep.ktxViewModelExtensions)
    implementation(Dep.ktxLiveDataExtensions)
    implementation(Dep.materialComponents)

    // betterlinkmovementmethod
    implementation(Dep.betterLinkMovementMethod)

    // crashlytics
    implementation(Dep.crashlytics)

    // firebase
    implementation(Dep.firebaseCore)

    // glide
    implementation(Dep.glide)
    kapt(Dep.glideProcessor)

    // networking
    implementation(Dep.gson)
    implementation(Dep.okHttp)
    implementation(Dep.okHttpLogger)
    implementation(Dep.retrofit)
    implementation(Dep.retrofitGsonConverter)

    // markdown
    implementation(Dep.markwonCore)
    implementation(Dep.markwonGlide)
    implementation(Dep.markwonStrikethrough)

    // logging
    implementation(Dep.timber)

    // play
    implementation(Dep.playCore)

    // room
    implementation(Dep.room)
    implementation(Dep.roomCoroutines)
    kapt(Dep.roomProcessor)

    // testing
    testImplementation(Dep.androidxTestCore)
    testImplementation(Dep.jUnit)
    testImplementation(Dep.mockito)
    testImplementation(Dep.mockitoKotlin)
    testImplementation(Dep.okHttpMock)
    testImplementation(Dep.robolectric)
    androidTestImplementation(Dep.testRunner)
    androidTestImplementation(Dep.espresso)
}

apply(plugin = "com.google.gms.google-services")
