object Versions {
    val kotlin = "1.1.51"
    val gradle = "3.0.1"
    val supportLib = "26.1.0"
    val constraintLayout = "1.0.2"
    val jUnit = "4.12"
    val testRunner = "1.0.1"
    val espresso = "3.0.1"
}

object Libs {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jre7:${Versions.kotlin}"
    val supportAppcompat = "com.android.support:appcompat-v7:${Versions.supportLib}"
    val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
}

object TestLibs {
    val jUnit = "junit:junit:${Versions.jUnit}"
    val testRunner = "com.android.support.test:runner:${Versions.testRunner}"
    val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
}

object Plugins {
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
}