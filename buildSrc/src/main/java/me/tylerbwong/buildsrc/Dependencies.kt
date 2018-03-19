object Versions {
    val kotlin = "1.2.30"
    val gradle = "3.0.1"
    val supportLib = "27.1.0"
    val constraintLayout = "1.0.2"
    val okHttp = "3.10.0"
    val retrofit = "2.4.0"
    val rxAndroid = "2.0.2"
    val rxJava = "2.1.10"
    val gson = "2.8.2"
    val jUnit = "4.12"
    val timber = "4.6.1"
    val testRunner = "1.0.1"
    val espresso = "3.0.1"
}

object Libs {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jre7:${Versions.kotlin}"
    val supportAppcompat = "com.android.support:appcompat-v7:${Versions.supportLib}"
    val cardView = "com.android.support:cardview-v7:${Versions.supportLib}"
    val supportDesign = "com.android.support:design:${Versions.supportLib}"
    val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    val okHttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    val gson = "com.google.code.gson:gson:${Versions.gson}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
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