import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlintGradle
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK)

    defaultConfig {
        minSdkVersion(AndroidConfig.MIN_SDK)
        targetSdkVersion(AndroidConfig.TARGET_SDK)
        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        buildConfig = false
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        apiVersion = "1.3"
        jvmTarget = "1.8"
    }

    lintOptions {
        isAbortOnError = false
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

ktlint {
    debug.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}

detekt {
    config = files("$rootDir/detekt.yml")
}

dependencies {
    implementation(Dep.kotlinLib)
    implementation(Dep.androidxCore)
    implementation(Dep.androidxRecyclerView)
}
