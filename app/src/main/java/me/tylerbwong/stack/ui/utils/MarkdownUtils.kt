package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.widget.TextView
import me.tylerbwong.stack.data.network.ServiceProvider
import ru.noties.markwon.AbstractMarkwonPlugin
import ru.noties.markwon.Markwon
import ru.noties.markwon.MarkwonConfiguration
import ru.noties.markwon.core.CorePlugin
import ru.noties.markwon.ext.strikethrough.StrikethroughPlugin
import ru.noties.markwon.image.ImagesPlugin
import ru.noties.markwon.image.okhttp.OkHttpImagesPlugin

object MarkdownUtils {
    lateinit var markwon: Markwon

    fun init(context: Context) {
        val configurationPlugin = object : AbstractMarkwonPlugin() {
            override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                builder
                        .urlProcessor(urlProcessor)
                        .linkResolver(tabsResolver)
            }
        }
        val plugins = listOf(
                CorePlugin.create(),
                ImagesPlugin.create(context),
                OkHttpImagesPlugin.create(ServiceProvider.okHttpClient),
                StrikethroughPlugin.create(),
                configurationPlugin
        )
        markwon = Markwon.builder(context)
                .usePlugins(plugins)
                .build()
    }
}

private val urlProcessor = CustomUrlProcessor()
private val tabsResolver = CustomTabsLinkResolver()
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
    MarkdownUtils.markwon.setMarkdown(this, markdown.stripSpecials())
}

private fun String.stripSpecials(): String {
    var result = this
    specialChars.forEach { (key, value) ->
        result = result.replace(key, value)
    }
    return result
}
