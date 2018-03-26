package me.tylerbwong.stack.presentation.utils

import android.widget.TextView
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.il.AsyncDrawableLoader

fun TextView.setMarkdown(markdown: String) {
    Markwon.setMarkdown(
            this,
            SpannableConfiguration.builder(this.context)
                    .urlProcessor(CustomUrlProcessor())
                    .linkResolver(CustomTabsLinkResolver())
                    .asyncDrawableLoader(AsyncDrawableLoader.create())
                    .build(),
            markdown
    )
}
