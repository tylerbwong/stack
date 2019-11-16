package me.tylerbwong.stack.ui.utils.markdown

import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import me.tylerbwong.stack.ui.ApplicationWrapper
import org.apache.commons.text.StringEscapeUtils

object Markdown {
    val markwon by lazy {
        val context = ApplicationWrapper.context
        val plugins = listOf(
            CoilImagesPlugin.create(context),
            HtmlPlugin.create(),
            StrikethroughPlugin.create(),
            TablePlugin.create(context),
            UrlPlugin.create()
        )
        Markwon.builder(context)
            .usePlugins(plugins)
            .build()
    }
}

fun TextView.setMarkdown(markdown: String) {
    Markdown.markwon.setMarkdown(this, markdown.stripSpecials())
}

private fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
