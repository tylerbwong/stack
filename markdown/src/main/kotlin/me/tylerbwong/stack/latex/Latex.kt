package me.tylerbwong.stack.latex

import android.widget.TextView
import io.noties.markwon.Markwon
import org.apache.commons.text.StringEscapeUtils
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LatexMarkdown

@Singleton
class Latex @Inject constructor(@LatexMarkdown private val markwon: Markwon) {

    fun setLatex(textView: TextView, markdown: String) {
        markwon.setMarkdown(textView, markdown.stripSpecials())
    }

    private fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
}
