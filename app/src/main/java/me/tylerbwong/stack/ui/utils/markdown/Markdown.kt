package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import org.apache.commons.text.StringEscapeUtils

object Markdown {
    lateinit var markwon: Markwon

    fun init(context: Context) {
        val plugins = listOf(
            CoilImagePlugin.create(context),
            HtmlPlugin.create(),
            StrikethroughPlugin.create(),
            TablePlugin.create(context),
            UrlPlugin.create()
        )
        markwon = Markwon.builder(context)
            .usePlugins(plugins)
            .build()
    }
}

fun TextView.setMarkdown(markdown: String) {
    Markdown.markwon.setMarkdown(this, markdown.stripSpecials())
}

private fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
