package me.tylerbwong.stack.latex

import android.widget.TextView
import io.noties.markwon.Markwon
import me.tylerbwong.stack.markdown.utils.stripSpecials
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
}
