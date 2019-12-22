package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle
import me.tylerbwong.stack.ui.ApplicationWrapper
import org.apache.commons.text.StringEscapeUtils

@PrismBundle(includeAll = true)
object Markdown {

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

    val markwon by lazy {
        val context = ApplicationWrapper.context
        val plugins = listOf(
            CoilImagesPlugin.create(context),
            HtmlPlugin.create(),
            StrikethroughPlugin.create(),
            TablePlugin.create(context),
            UrlPlugin.create()
        )
        val experimentalPlugins = if (experimentalSyntaxHighlightingEnabled) {
            listOf(
                SyntaxHighlightPlugin.create(
                    Prism4j(GrammarLocatorDef()),
                    Prism4jThemeDarkula.create()
                )
            )
        } else {
            emptyList()
        }

        Markwon.builder(context)
            .usePlugins(plugins + experimentalPlugins)
            .build()
    }
}

fun TextView.setMarkdown(markdown: String) {
    Markdown.markwon.setMarkdown(this, markdown.stripSpecials())
}

private fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
