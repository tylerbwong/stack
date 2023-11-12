import com.android.build.gradle.internal.tasks.databinding.DataBindingGenBaseClassesTask
import dagger.hilt.android.plugin.util.capitalize
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool
import java.util.Locale

plugins {
    id("com.android.application")
    `kotlin-android`
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.dagger.hilt)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.mikepenz.aboutlibraries.plugin")
    StackPlugin
}

android {
    namespace = "me.tylerbwong.stack"
    defaultConfig {
        testInstrumentationRunner = "me.tylerbwong.stack.StackTestRunner"

        buildConfigField("String", "CLIENT_ID", stringProperty("stackClientId"))
        resValue("integer", "version_code", "${AndroidConfig.VERSION_CODE}")
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }
}

// Workaround for https://github.com/google/dagger/issues/4049
androidComponents {
    onVariants {
        afterEvaluate {
            val variantName = it.name.capitalize(Locale.getDefault())
            val dataBindingTask = tasks.named(
                "dataBindingGenBaseClasses$variantName"
            ).get() as? DataBindingGenBaseClassesTask
            if (dataBindingTask != null) {
                tasks.getByName("ksp${variantName}Kotlin") {
                    (this as AbstractKotlinCompileTool<*>).source(dataBindingTask.sourceOutFolder)
                }
            }
        }
    }
}

ksp {
    arg("room.incremental", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
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
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.recyclerview)
    ksp(libs.androidx.room.compiler)
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
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)

    // google
    playImplementation(platform(libs.google.firebase.bom))
    playImplementation(libs.google.firebase.analytics.ktx)
    playImplementation(libs.google.firebase.crashlytics)
    implementation(libs.google.material)

    // licenses
    implementation(libs.aboutLibraries.core)
    implementation(libs.aboutLibraries.compose)

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
    implementation(libs.coil.compose)

    // dagger
    ksp(libs.google.dagger.hilt.android.compiler)
    implementation(libs.google.dagger.hilt.android)

    // insetter
    implementation(libs.insetter)

    // logging
    implementation(libs.timber)

    // markdown
    implementation(projects.markdown)
    implementation(libs.apache.commonsText)
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
    implementation(libs.betterlinkmovementmethod)
    implementation(libs.processPhoenix)

    // networking
    implementation(projects.stackexchangeApi)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlinCodegen)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)

    // play
    playImplementation(libs.google.play.appUpdate)
    playImplementation(libs.google.play.billing)
    playImplementation(libs.google.play.review)

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
