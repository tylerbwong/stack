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
    implementation("com.android.tools.build:builder:7.1.0-alpha01")
    implementation("com.android.tools.build:builder-model:7.1.0-alpha01")
    implementation("com.android.tools.build:gradle:7.1.0-alpha01")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.17.0")
}
