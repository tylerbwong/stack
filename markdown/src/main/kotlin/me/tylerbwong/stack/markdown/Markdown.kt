package me.tylerbwong.stack.markdown

import android.text.Spanned
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonReducer
import io.noties.prism4j.annotations.PrismBundle
import org.apache.commons.text.StringEscapeUtils
import org.commonmark.node.Node
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MarkdownMarkwon

@Singleton
@PrismBundle(includeAll = true)
class Markdown @Inject constructor(@MarkdownMarkwon private val markwon: Markwon) : Renderer {

    private val reducer = MarkwonReducer.directChildren()

    fun setMarkdown(textView: TextView, markdown: String) {
        markwon.setMarkdown(textView, markdown.stripSpecials())
    }

    fun setMarkdown(textView: TextView, node: Node) {
        markwon.setParsedMarkdown(textView, markwon.render(node))
    }

    fun setMarkdown(textView: TextView, spanned: Spanned) {
        markwon.setParsedMarkdown(textView, spanned)
    }

    fun parse(markdown: String): Node = markwon.parse(markdown.stripSpecials())

    fun reduce(node: Node): List<Node> = reducer.reduce(node)

    override fun render(node: Node): Spanned = markwon.render(node)

    private fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
}
