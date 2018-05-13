object Versions {
    val kotlin = "1.2.41"
    val gradlePlugin = "3.2.0-alpha14"
    val supportLib = "28.0.0-alpha1"
    val constraintLayout = "1.1.0"
    val glide = "4.7.1"
    val markwon = "1.0.4"
    val okHttp = "3.10.0"
    val retrofit = "2.4.0"
    val rxAndroid = "2.0.2"
    val rxJava = "2.1.13"
    val stetho = "1.5.0"
    val gson = "2.8.4"
    val timber = "4.7.0"
    val room = "1.1.0"
    val jUnit = "4.12"
    val testRunner = "1.0.1"
    val espresso = "3.0.1"
}

object Libs {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jre7:${Versions.kotlin}"
    val supportAppcompat = "com.android.support:appcompat-v7:${Versions.supportLib}"
    val cardView = "com.android.support:cardview-v7:${Versions.supportLib}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val glideProcessor = "com.github.bumptech.glide:compiler:${Versions.glide}"
    val supportDesign = "com.android.support:design:${Versions.supportLib}"
    val supportCustomTabs = "com.android.support:customtabs:${Versions.supportLib}"
    val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    val markwon = "ru.noties:markwon:${Versions.markwon}"
    val markwonImageLoader = "ru.noties:markwon-image-loader:${Versions.markwon}"
    val okHttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"
    val stethoOkHttp = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"
    val gson = "com.google.code.gson:gson:${Versions.gson}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val room = "android.arch.persistence.room:runtime:${Versions.room}"
    val roomProcessor = "android.arch.persistence.room:compiler:${Versions.room}"
    val roomRxSupport = "android.arch.persistence.room:rxjava2:${Versions.room}"
}

object TestLibs {
    val jUnit = "junit:junit:${Versions.jUnit}"
    val testRunner = "com.android.support.test:runner:${Versions.testRunner}"
    val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
}

object Plugins {
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val gradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
}
