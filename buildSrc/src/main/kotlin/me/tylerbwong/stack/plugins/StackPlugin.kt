package me.tylerbwong.stack.plugins

import AndroidConfig
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.util.Locale

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
            configureCommonOptions(this@configureAppPlugin)

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
                    isShrinkResources = true
                }
            }

            flavorDimensions += "releaseType"
            productFlavors {
                register("base") {
                    dimension = "releaseType"
                }
                register("play") {
                    dimension = "releaseType"
                }
            }

            afterEvaluate {
                applicationVariants.all {
                    val formattedName = name.capitalize(Locale.getDefault())
                    with(tasks) {
                        listOfNotNull(
                            findByName("process${formattedName}GoogleServices"),
                            findByName("injectCrashlyticsMappingFileId$formattedName"),
                            findByName("uploadCrashlyticsMappingFile$formattedName")
                        ).forEach { it.enabled = flavorName == "play" }
                    }
                }
            }

            (sourceSets) {
                "base" {
                    java {
                        srcDir("src/base/kotlin")
                    }
                }
                "play" {
                    java {
                        srcDir("src/play/kotlin")
                    }
                }
            }
        }
    }

    private fun BaseExtension.configureCommonOptions(project: Project) {
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

        @Suppress("deprecation") // Move to CommonExtension
        lintOptions {
            isAbortOnError = false
        }

        packagingOptions {
            resources.excludes += setOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        }

        testOptions {
            unitTests.isIncludeAndroidResources = true
        }

        project.tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
            }
        }
    }

    private fun Project.configureStaticAnalysis() {
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
        apply(plugin = "io.gitlab.arturbosch.detekt")

        extensions.getByType<KtlintExtension>().apply {
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
