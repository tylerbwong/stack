import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlintGradle
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

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
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

    composeOptions {
        kotlinCompilerVersion = Versions.composeKotlinCompiler
        kotlinCompilerExtensionVersion = Versions.compose
    }

    kotlinOptions {
        apiVersion = "1.3"
        jvmTarget = "1.8"
    }

    lintOptions {
        isAbortOnError = false
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

ktlint {
    debug.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
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
    implementation(Dep.androidxActivity)
    implementation(Dep.androidxAppCompat)
    implementation(Dep.androidxBrowser)
    implementation(Dep.androidxCore)
    implementation(Dep.androidxFragment)
    implementation(Dep.androidxLiveData)
    implementation(Dep.androidxPreference)
    implementation(Dep.androidxRecyclerView)
    implementation(Dep.androidxSecurity)
    implementation(Dep.androidxSwipeRefreshLayout)
    implementation(Dep.androidxViewModel)
    implementation(Dep.androidxViewPager2)
    implementation(Dep.androidxWork)
    implementation(Dep.constraintLayout)
    implementation(Dep.materialComponents)

    // compose
    implementation(Dep.composeCore)
    implementation(Dep.composeFoundation)
    implementation(Dep.composeLayout)
    implementation(Dep.composeMaterial)
    implementation(Dep.composeTooling)

    // debug
    debugImplementation(Dep.hyperionAttr)
    debugImplementation(Dep.hyperionCore)
    debugImplementation(Dep.hyperionDisk)
    debugImplementation(Dep.hyperionMeasure)
    debugImplementation(Dep.hyperionSharedPref)
    debugImplementation(Dep.hyperionTimber)
    debugImplementation(Dep.chucker)
    releaseImplementation(Dep.chuckerNoOp)

    // dynamic-list-adapter
    implementation(project(":dynamic-list-adapter"))
    implementation(project(":dynamic-list-adapter-viewbinding"))

    // klock date/time
    implementation(Dep.klock)

    // betterlinkmovementmethod
    implementation(Dep.betterLinkMovementMethod)

    // coil
    implementation(Dep.coil)

    // dagger
    implementation(Dep.dagger)
    kapt(Dep.daggerCompiler)

    // firebase
    implementation(Dep.firebaseAnalytics)
    implementation(Dep.firebaseCrashlytics)

    // insetter
    implementation(Dep.insetter)

    // logging
    implementation(Dep.timber)

    // markdown
    implementation(Dep.apacheCommonsText)
    implementation(Dep.markwonCore)
    implementation(Dep.markwonHtml)
    implementation(Dep.markwonImageCoil)
    implementation(Dep.markwonLinkify)
    implementation(Dep.markwonStrikethrough)
    implementation(Dep.markwonSyntaxHighlight)
    implementation(Dep.markwonTables)
    kapt(Dep.prism4jBundler)

    // misc
    implementation(Dep.processPhoenix)

    // networking
    implementation(Dep.moshi)
    kapt(Dep.moshiKotlinCodegen)
    implementation(Dep.okHttp)
    implementation(Dep.okHttpLogger)
    implementation(Dep.retrofit)
    implementation(Dep.retrofitMoshiConverter)

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
