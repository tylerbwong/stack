plugins {
    id("com.android.application")
    `kotlin-android`
    `kotlin-kapt`
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("com.mikepenz.aboutlibraries.plugin")
    StackPlugin
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLIENT_ID", stringProperty("stackClientId"))

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.incremental", "true")
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerVersion = Versions.kotlin
        kotlinCompilerExtensionVersion = Versions.compose
    }
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
    implementation(Dep.androidxConstraintLayout)
    implementation(Dep.androidxCore)
    implementation(Dep.androidxFragment)
    kapt(Dep.androidxHiltCompiler)
    implementation(Dep.androidxHiltViewModel)
    implementation(Dep.androidxHiltWork)
    implementation(Dep.androidxLiveData)
    implementation(Dep.androidxPreference)
    implementation(Dep.androidxRecyclerView)
    implementation(Dep.androidxRoomRuntime)
    implementation(Dep.androidxRoomKtx)
    kapt(Dep.androidxRoomProcessor)
    implementation(Dep.androidxSecurity)
    implementation(Dep.androidxSwipeRefreshLayout)
    implementation(Dep.androidxViewModel)
    implementation(Dep.androidxViewPager2)
    implementation(Dep.androidxWork)
    implementation(Dep.materialComponents)

    // compose
    implementation(markdownCompose())
    implementation(Dep.composeCore)
    implementation(Dep.composeFoundation)
    implementation(Dep.composeLayout)
    implementation(Dep.composeLiveData)
    implementation(Dep.composeMaterial)
    implementation(Dep.composeMaterialIcons)
    implementation(Dep.composeTooling)

    // licenses
    implementation(Dep.aboutLibrariesCore)

    // debug
    debugImplementation(Dep.chucker)
    releaseImplementation(Dep.chuckerNoOp)

    // dynamic-list-adapter
    implementation(dynamicListAdapter())
    implementation(dynamicListAdapterViewBinding())

    // klock date/time
    implementation(Dep.klock)

    // coil
    implementation(Dep.coil)
    implementation(Dep.coilAccompanist)

    // dagger
    implementation(Dep.daggerHiltAndroid)
    kapt(Dep.daggerHiltAndroidCompiler)

    // firebase
    implementation(Dep.firebaseAnalytics)
    implementation(Dep.firebaseCrashlytics)

    // insetter
    implementation(Dep.insetter)

    // logging
    implementation(Dep.timber)

    // markdown
    implementation(markdown())
    implementation(Dep.markwonCore)
    implementation(Dep.markwonHtml)
    implementation(Dep.markwonImageCoil)
    implementation(Dep.markwonLinkify)
    implementation(Dep.markwonStrikethrough)
    implementation(Dep.markwonSyntaxHighlight)
    implementation(Dep.markwonTables)
    implementation(Dep.markwonTaskList)

    // misc
    implementation(Dep.processPhoenix)

    // networking
    implementation(stackExchangeApi())
    implementation(Dep.moshi)
    kapt(Dep.moshiKotlinCodegen)
    implementation(Dep.okHttp)
    implementation(Dep.okHttpLogger)
    implementation(Dep.retrofit)
    implementation(Dep.retrofitMoshiConverter)

    // play
    implementation(Dep.playCore)

    // testing
    testImplementation(Dep.androidxTestCore)
    testImplementation(Dep.jUnit)
    testImplementation(Dep.mockito)
    testImplementation(Dep.mockitoKotlin)
    testImplementation(Dep.okHttpMock)
    testImplementation(Dep.robolectric)
    androidTestImplementation(Dep.androidxTestCore)
    androidTestImplementation(Dep.androidxTestExt)
    androidTestImplementation(Dep.androidxTestRunner)
    androidTestImplementation(Dep.barista)
    androidTestImplementation(Dep.espresso)
    androidTestImplementation(Dep.espressoIntents)
}

val googleServices = file("google-services.json")
if (!googleServices.exists()) {
    file("fake-google-services.json").copyTo(googleServices, overwrite = true)
}

apply(plugin = "com.google.gms.google-services")
