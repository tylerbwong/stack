plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:builder:7.0.0-beta01")
    implementation("com.android.tools.build:builder-model:7.0.0-beta01")
    implementation("com.android.tools.build:gradle:7.0.0-beta01")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.16.0")
}
