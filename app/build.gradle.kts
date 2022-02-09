plugins {
    id("com.android.application")
    `kotlin-android`
    `kotlin-kapt`
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.mikepenz.aboutlibraries.plugin")
    StackPlugin
}

android {
    defaultConfig {
        testInstrumentationRunner = "me.tylerbwong.stack.StackTestRunner"

        buildConfigField("String", "CLIENT_ID", stringProperty("stackClientId"))
        resValue("integer", "version_code", "${AndroidConfig.VERSION_CODE}")

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
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.get()
    }
}

dependencies {
    // kotlin
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    // androidx
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.recyclerview)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.work.runtime.ktx)

    // compose
    implementation(projects.composePreference)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)

    // google
    implementation(libs.coil.compose)
    playImplementation(platform(libs.google.firebase.bom))
    playImplementation(libs.google.firebase.analytics.ktx)
    playImplementation(libs.google.firebase.crashlytics)
    implementation(libs.google.material)

    // licenses
    implementation(libs.aboutLibraries.core)

    // debug
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.noop)

    // dynamic-list-adapter
    implementation(projects.dynamicListAdapter)
    implementation(projects.dynamicListAdapterViewbinding)

    // klock date/time
    implementation(libs.klock)

    // coil
    implementation(libs.coil)

    // dagger
    kapt(libs.google.dagger.hilt.android.compiler)
    implementation(libs.google.dagger.hilt.android)

    // insetter
    implementation(libs.insetter)

    // logging
    implementation(libs.timber)

    // markdown
    implementation(projects.markdown)
    implementation(libs.markwon.core)
    implementation(libs.markwon.html)
    implementation(libs.markwon.imageCoil)
    implementation(libs.markwon.inlineParser)
    implementation(libs.markwon.latex)
    implementation(libs.markwon.linkify)
    implementation(libs.markwon.strikethrough)
    implementation(libs.markwon.syntaxhighlight)
    implementation(libs.markwon.tables)
    implementation(libs.markwon.tasklist)

    // misc
    implementation(libs.processPhoenix)

    // networking
    implementation(projects.stackexchangeApi)
    implementation(libs.moshi)
    kapt(libs.moshi.kotlinCodegen)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)

    // play
    playImplementation(libs.google.playCore)

    // testing
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.barista)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intents)
}

val googleServices = file("google-services.json")
if (!googleServices.exists()) {
    file("fake-google-services.json").copyTo(googleServices, overwrite = true)
}
