plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(lib.android.gradlePlugin)
    implementation(lib.jetbrains.kotlin.gradlePlugin)
    implementation(lib.ktlint.gradlePlugin)
    implementation(lib.detekt.gradlePlugin)
    implementation(lib.javaPoet)
}
