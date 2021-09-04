buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.about.libraries.gradle)
        classpath(libs.android.tools.gradle)
        classpath(libs.dagger.hilt.gradle)
        classpath(libs.detekt.gradle)
        classpath(libs.google.firebase.crashlytics.gradle)
        classpath(libs.google.services)
        classpath(libs.jetbrains.kotlin.gradle)
        classpath(libs.ktlint.gradle)
        classpath(libs.metalava.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jitpack()
    }

    configurations.all {
        // https://github.com/noties/Markwon/issues/148
        exclude(group = "org.jetbrains", module = "annotations-java5")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
