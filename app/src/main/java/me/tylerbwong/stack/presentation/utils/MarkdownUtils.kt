package me.tylerbwong.stack.presentation.utils

import android.widget.TextView
import me.tylerbwong.stack.data.network.ServiceProvider
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration

private val urlProcessor = CustomUrlProcessor()
private val tabsResolver = CustomTabsLinkResolver()

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
