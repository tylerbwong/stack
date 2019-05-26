package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.widget.TextView
import me.tylerbwong.stack.data.network.ServiceProvider
import ru.noties.markwon.Markwon
import ru.noties.markwon.ext.strikethrough.StrikethroughPlugin
import ru.noties.markwon.image.ImagesPlugin
import ru.noties.markwon.image.okhttp.OkHttpImagesPlugin

object Markdown {
    lateinit var markwon: Markwon

    fun init(context: Context) {
        val plugins = listOf(
                GlideImagePlugin.create(context),
                ImagesPlugin.create(context),
                OkHttpImagesPlugin.create(ServiceProvider.okHttpClient),
                StrikethroughPlugin.create(),
                UrlPlugin.create()
        )
        markwon = Markwon.builder(context)
                .usePlugins(plugins)
                .build()
    }
}

private val specialChars = mapOf(
        "&lt;" to "<",
        "&gt;" to ">",
        "&quot;" to "\"",
        "&nbsp;" to " ",
        "&amp;" to "&",
        "&apos;" to "'",
        "&#39;" to "'",
        "&#40;" to "(",
        "&#41;" to ")",
        "&#215;" to "×"
)

fun TextView.setMarkdown(markdown: String) {
    Markdown.markwon.setMarkdown(this, markdown.stripSpecials())
}

private fun String.stripSpecials(): String {
    var result = this
    specialChars.forEach { (key, value) ->
        result = result.replace(key, value)
    }
    return result
}
