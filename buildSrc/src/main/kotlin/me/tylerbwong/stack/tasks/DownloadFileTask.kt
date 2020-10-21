package me.tylerbwong.stack.tasks

import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class DownloadFileTask : DefaultTask() {
    @get:Input
    abstract val url: Property<String>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @TaskAction
    fun taskAction() {
        if (output.asFile.get().exists()) {
            return
        }
        val client = OkHttpClient()
        val request = Request.Builder().get().url(url.get()).build()

        client.newCall(request).execute().body!!.byteStream().use { body ->
            output.asFile.get().outputStream().buffered().use { file ->
                body.copyTo(file)
            }
        }
    }
}
