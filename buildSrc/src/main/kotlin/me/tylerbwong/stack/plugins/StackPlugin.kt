package me.tylerbwong.stack.plugins

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

class StackPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.all {
            when (this) {
                is LibraryPlugin -> configureLibraryPlugin(target)
                is AppPlugin -> configureAppPlugin(target)
            }
        }
        configureStaticAnalysis(target)
    }

    private fun configureLibraryPlugin(project: Project) {
        project.extensions.getByType(LibraryExtension::class).apply {
            configureCommonOptions()
        }
    }

    private fun configureAppPlugin(project: Project) {
        project.extensions.getByType(BaseAppModuleExtension::class).apply {
            configureCommonOptions()
            defaultConfig {
                applicationId = AndroidConfig.APPLICATION_ID
                versionCode = AndroidConfig.VERSION_CODE
                versionName = AndroidConfig.VERSION_NAME
            }
        }
    }

    private fun TestedExtension.configureCommonOptions() {
        compileSdkVersion(AndroidConfig.COMPILE_SDK)

        defaultConfig {
            minSdkVersion(AndroidConfig.MIN_SDK)
            targetSdkVersion(AndroidConfig.TARGET_SDK)
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        lintOptions {
            isAbortOnError = false
        }

        testOptions {
            unitTests.isIncludeAndroidResources = true
        }
    }

    private fun configureStaticAnalysis(project: Project) {
        with(project) {
            apply(plugin = "org.jlleitschuh.gradle.ktlint")
            apply(plugin = "io.gitlab.arturbosch.detekt")

            extensions.getByType(KtlintExtension::class).apply {
                debug.set(true)
                reporters {
                    reporter(ReporterType.CHECKSTYLE)
                }
            }

            extensions.getByType(DetektExtension::class).apply {
                config = files("$rootDir/detekt.yml")
            }
        }
    }
}
