package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import me.tylerbwong.stack.ui.utils.GlideApp

object Markdown {
    lateinit var markwon: Markwon

    fun init(context: Context) {
        val plugins = listOf(
                GlideImagesPlugin.create(GlideApp.with(context)),
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
