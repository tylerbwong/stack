package me.tylerbwong.stack.presentation.utils

import android.widget.TextView
import me.tylerbwong.stack.data.network.ServiceProvider
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration

object MarkdownUtils {

    private val urlProcessor = CustomUrlProcessor()
    private val tabsResolver = CustomTabsLinkResolver()

    fun setMarkdown(textView: TextView, markdown: String) {
        Markwon.setMarkdown(
                textView,
                SpannableConfiguration.builder(textView.context)
                        .urlProcessor(urlProcessor)
                        .linkResolver(tabsResolver)
                        .asyncDrawableLoader(ServiceProvider.asyncDrawableLoader)
                        .build(),
                markdown.stripSpecials()
        )
    }
}
