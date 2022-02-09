plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

// TODO Migrate these to version catalog accessors
dependencies {
    implementation("com.android.tools.build:gradle:7.1.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.2.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
    implementation("com.squareup:javapoet:1.13.0")
}
