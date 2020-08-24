plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    StackPlugin
}

android {
    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    implementation(Dep.kotlinLib)

    // androidx
    implementation(Dep.androidxAppCompat)

    // betterlinkmovementmethod
    implementation(Dep.betterLinkMovementMethod)

    // dagger
    implementation(Dep.daggerHiltAndroid)
    kapt(Dep.daggerHiltAndroidCompiler)

    // markdown
    implementation(Dep.apacheCommonsText)
    implementation(Dep.markwonCore)
    implementation(Dep.markwonSyntaxHighlight)
    kapt(Dep.prism4jBundler)
}
