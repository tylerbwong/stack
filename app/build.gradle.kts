plugins {
    id("com.android.application")
    `kotlin-android`
    `kotlin-kapt`
    id("dagger.hilt.android.plugin")
    id("com.facebook.testing.screenshot")
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
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    // kotlin
    implementation(libs.jetbrains.kotlin.stdlib.jdk8)
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
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.material.material)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.ui)

    // google
    implementation(libs.google.accompanist.coil)
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics.ktx)
    implementation(libs.google.firebase.crashlytics.core)
    implementation(libs.google.material)

    // licenses
    implementation(libs.about.libraries.core)

    // debug
    debugImplementation(libs.chucker.library.core)
    releaseImplementation(libs.chucker.library.noop)

    // dynamic-list-adapter
    implementation(projects.dynamicListAdapter)
    implementation(projects.dynamicListAdapterViewbinding)

    // klock date/time
    implementation(libs.klock)

    // coil
    implementation(libs.coil)

    // dagger
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.dagger.hilt.android.core)

    // insetter
    implementation(libs.insetter)

    // logging
    implementation(libs.timber)

    // markdown
    implementation(projects.markdown)
    implementation(libs.markwon.core)
    implementation(libs.markwon.html)
    implementation(libs.markwon.image.coil)
    implementation(libs.markwon.inline.parser)
    implementation(libs.markwon.latex)
    implementation(libs.markwon.linkify)
    implementation(libs.markwon.strikethrough)
    implementation(libs.markwon.syntaxhighlight)
    implementation(libs.markwon.tables)
    implementation(libs.markwon.tasklist)

    // misc
    implementation(libs.process.phoenix)

    // networking
    implementation(projects.stackexchangeApi)
    implementation(libs.moshi.core)
    kapt(libs.moshi.kotlin.codegen)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)

    // play
    implementation(libs.google.play.core)

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

apply(plugin = "com.google.gms.google-services")
