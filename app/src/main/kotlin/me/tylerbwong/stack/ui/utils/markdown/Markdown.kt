package me.tylerbwong.stack.ui.utils.markdown

import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.prism4j.annotations.PrismBundle
import org.apache.commons.text.StringEscapeUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@PrismBundle(includeAll = true)
class Markdown @Inject constructor(private val markwon: Markwon) {

    fun setMarkdown(textView: TextView, markdown: String) {
        markwon.setMarkdown(textView, markdown.stripSpecials())
    }

    private fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
}
