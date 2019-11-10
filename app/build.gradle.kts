import io.gitlab.arturbosch.detekt.detekt

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("io.fabric")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK)

    defaultConfig {
        applicationId = AndroidConfig.APPLICATION_ID
        minSdkVersion(AndroidConfig.MIN_SDK)
        targetSdkVersion(AndroidConfig.TARGET_SDK)
        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
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

    kotlinOptions {
        jvmTarget = "1.8"
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

detekt {
    config = files("$rootDir/detekt.yml")
}

dependencies {
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
    implementation(Dep.androidxViewPager2)
    implementation(Dep.constraintLayout)
    implementation(Dep.ktxActivityExtensions)
    implementation(Dep.ktxFragmentExtensions)
    implementation(Dep.ktxViewModelExtensions)
    implementation(Dep.ktxLiveDataExtensions)
    implementation(Dep.materialComponents)

    // klock date/time
    implementation(Dep.klock)

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
    implementation(Dep.apacheCommonsText)
    implementation(Dep.markwonCore)
    implementation(Dep.markwonGlide)
    implementation(Dep.markwonHtml)
    implementation(Dep.markwonStrikethrough)
    implementation(Dep.markwonTables)

    // logging
    implementation(Dep.timber)

    // play
    implementation(Dep.playCore)

    // room
    implementation(Dep.roomRuntime)
    implementation(Dep.roomKtx)
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
