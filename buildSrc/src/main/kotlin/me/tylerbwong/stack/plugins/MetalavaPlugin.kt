package me.tylerbwong.stack.plugins

import com.android.build.gradle.LibraryExtension
import me.tylerbwong.stack.tasks.DownloadFileTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.findByType
import java.io.File
import java.util.Locale

open class MetalavaExtension {
    var version = "1.3.0-SNAPSHOT"
    var format = "v3"
    var outputFileName = "api.txt"
}

class MetalavaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            afterEvaluate {
                val extension = extensions.create("metalava", MetalavaExtension::class.java)
                val downloadMetalavaTaskProvider = tasks.register(
                    "downloadMetalava",
                    DownloadFileTask::class.java
                ) {
                    url.set("https://storage.googleapis.com/android-ci/metalava-full-${extension.version}.jar")
                    output.set(layout.buildDirectory.file("${rootProject.buildDir}/metalava/metalava.jar"))
                }
                // TODO Support non-Android projects if needed
                val libraryExtension = extensions.findByType<LibraryExtension>()
                    ?: throw GradleException("Metalava not available for non-library modules")
                val debugVariant = libraryExtension.libraryVariants.find {
                    it.name.contains("debug", ignoreCase = true)
                } ?: throw GradleException("Metalava not available for non-debug variants")
                tasks.register("metalava", JavaExec::class.java) {
                    @Suppress("UnstableApiUsage")
                    classpath(downloadMetalavaTaskProvider.flatMap { it.output.asFile })

                    val androidClasspath = libraryExtension.bootClasspath
                    val externalClasspath =
                        debugVariant.getCompileClasspath(null).filter { it.exists() }
                    val fullClasspath = (androidClasspath + externalClasspath).joinToString(":")
                    val sources = file("src").walk()
                        .maxDepth(2)
                        .onEnter { !it.name.toLowerCase(Locale.getDefault()).contains("test") }
                        .filter { it.isDirectory && (it.name == "java" || it.name == "kotlin") }
                        .toList()

                    val hides = sources.flatMap { file ->
                        file.walk().filter { it.isDirectory && it.name == "internal" }.toList()
                    }.map {
                        it.relativeTo(projectDir).path
                            .split(File.separator)
                            .drop(3)
                            .joinToString(".")
                    }.distinct()

                    val sourcePaths = listOf("--source-path") + sources.joinToString(":")
                    val hidePackages = hides.flatMap { listOf("--hide-package", it) }

                    val args = listOf(
                        "--protected",
                        "--no-banner",
                        "--format=${extension.format}",
                        "--api", extension.outputFileName,
                        "--classpath", fullClasspath
                    ) + sourcePaths + hidePackages

                    isIgnoreExitValue = true
                    setArgs(args)
                }
            }
        }
    }
}
