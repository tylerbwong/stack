package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.prism4j.annotations.PrismBundle
import me.tylerbwong.stack.ui.ApplicationWrapper
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

    companion object {
        private const val MARKDOWN_SHARED_PREFS = "markdown_shared_prefs"
        private const val MARKDOWN_SYNTAX_HIGHLIGHT = "markdown_syntax_highlight"

        var experimentalSyntaxHighlightingEnabled: Boolean
            get() {
                val preferences = ApplicationWrapper.context.getSharedPreferences(
                    MARKDOWN_SHARED_PREFS,
                    Context.MODE_PRIVATE
                )
                return preferences.getBoolean(MARKDOWN_SYNTAX_HIGHLIGHT, false)
            }
            set(value) {
                val preferences = ApplicationWrapper.context.getSharedPreferences(
                    MARKDOWN_SHARED_PREFS,
                    Context.MODE_PRIVATE
                )
                preferences.edit().putBoolean(MARKDOWN_SYNTAX_HIGHLIGHT, value).apply()
            }
    }
}
