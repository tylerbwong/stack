buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.aboutLibraries.gradlePlugin)
        classpath(libs.android.gradlePlugin)
        classpath(libs.google.dagger.hilt.gradlePlugin)
        classpath(libs.google.firebase.crashlytics.gradlePlugin)
        classpath(libs.google.services.gradlePlugin)
        classpath(libs.detekt.gradlePlugin)
        classpath(libs.jetbrains.kotlin.gradlePlugin)
        classpath(libs.ktlint.gradlePlugin)
        classpath(libs.metalava.gradlePlugin)
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
