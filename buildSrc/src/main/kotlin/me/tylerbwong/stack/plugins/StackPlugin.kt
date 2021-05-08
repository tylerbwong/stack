package me.tylerbwong.stack.plugins

import AndroidConfig
import Versions
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
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

class StackPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.apply<Project> {
            plugins.all {
                when (this) {
                    is LibraryPlugin -> configureLibraryPlugin()
                    is AppPlugin -> configureAppPlugin()
                }
            }

            configureStaticAnalysis()
        }
    }

    private fun Project.configureLibraryPlugin() {
        extensions.getByType<LibraryExtension>().apply {
            configureCommonOptions(this@configureLibraryPlugin)
        }
    }

    private fun Project.configureAppPlugin() {
        extensions.getByType<BaseAppModuleExtension>().apply {
            configureCommonOptions(project)

            defaultConfig {
                applicationId = AndroidConfig.APPLICATION_ID
                versionCode = AndroidConfig.VERSION_CODE
                versionName = AndroidConfig.VERSION_NAME
            }

            buildTypes {
                getByName("debug") {
                    applicationIdSuffix = ".debug"
                    versionNameSuffix = "-debug"
                }
                getByName("release") {
                    // TODO https://issuetracker.google.com/issues/186806256
                    // isShrinkResources = true
                }
            }
        }
    }

    private fun TestedExtension.configureCommonOptions(project: Project) {
        compileSdkVersion(AndroidConfig.COMPILE_SDK)

        defaultConfig {
            minSdk = AndroidConfig.MIN_SDK
            targetSdk = AndroidConfig.TARGET_SDK
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

        packagingOptions {
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
        }

        testOptions {
            unitTests.isIncludeAndroidResources = true
        }

        project.tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                freeCompilerArgs += listOf(
                    "-Xopt-in=kotlin.RequiresOptIn",
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
                )
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }

        sourceSets["main"].java.srcDir("src/main/kotlin")
        sourceSets["test"].java.srcDir("src/test/kotlin")
        sourceSets["androidTest"].java.srcDir("src/androidTest/kotlin")
    }

    private fun Project.configureStaticAnalysis() {
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
        apply(plugin = "io.gitlab.arturbosch.detekt")

        extensions.getByType<KtlintExtension>().apply {
            version.set(Versions.ktlint)
            debug.set(true)
            reporters {
                reporter(ReporterType.CHECKSTYLE)
            }
        }

        extensions.getByType<DetektExtension>().apply {
            config = files("$rootDir/detekt.yml")
        }
    }
}
