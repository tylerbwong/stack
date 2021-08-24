// TODO Migrate these to version catalog accessors
object Versions {
    const val aboutLibraries = "8.9.1"
    const val androidGradlePlugin = "7.1.0-alpha09"
    const val daggerHilt = "2.38.1"
    const val detekt = "1.18.0"
    const val firebaseCrashlyticsGradlePlugin = "2.7.1"
    const val googleServices = "4.3.8"
    const val kotlin = "1.5.21"
    const val ktlintGradle = "10.1.0"
    const val metalavaGradlePlugin = "0.1.9"
    const val screenshotTesting = "0.14.0"
}

object Dep {
    const val aboutLibrariesGradlePlugin = "com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:${Versions.aboutLibraries}"
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val daggerHiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.daggerHilt}"
    const val detektGradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val firebaseCrashlyticsGradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlyticsGradlePlugin}"
    const val googleServicesPlugin = "com.google.gms:google-services:${Versions.googleServices}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val ktlintGradlePlugin = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlintGradle}"
    const val metalavaGradlePlugin = "me.tylerbwong.gradle:metalava-gradle:${Versions.metalavaGradlePlugin}"
    const val screenshotTestingPlugin = "com.facebook.testing.screenshot:plugin:${Versions.screenshotTesting}"
}
