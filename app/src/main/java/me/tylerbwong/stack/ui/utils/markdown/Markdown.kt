package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.prism4j.annotations.PrismBundle
import me.tylerbwong.stack.ui.ApplicationWrapper
import org.apache.commons.text.StringEscapeUtils
import javax.inject.Inject

@PrismBundle(includeAll = true)
class Markdown {

    @Inject
    lateinit var markwon: Markwon

    init {
        ApplicationWrapper.uiComponent.inject(this)
    }

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

fun TextView.setMarkdown(markdown: String) {
    Markdown().markwon.setMarkdown(this, markdown.stripSpecials())
}

private fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
