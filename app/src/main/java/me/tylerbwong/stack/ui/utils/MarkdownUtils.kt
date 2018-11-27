package me.tylerbwong.stack.ui.utils

import android.widget.TextView
import me.tylerbwong.stack.data.network.ServiceProvider
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration

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
    Markwon.setMarkdown(
            this,
            SpannableConfiguration.builder(context)
                    .urlProcessor(urlProcessor)
                    .linkResolver(tabsResolver)
                    .asyncDrawableLoader(ServiceProvider.asyncDrawableLoader)
                    .build(),
            markdown.stripSpecials()
    )
}

private fun String.stripSpecials(): String {
    var result = this
    specialChars.forEach { (key, value) ->
        result = result.replace(key, value)
    }
    return result
}
