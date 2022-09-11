package me.tylerbwong.stack.markdown

import android.text.Spanned
import android.widget.TextView
import androidx.annotation.WorkerThread
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonReducer
import io.noties.prism4j.annotations.PrismBundle
import me.tylerbwong.stack.markdown.utils.stripSpecials
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
    private val languageBlockMapping = mapOf(
        "c++" to "cpp",
        "kt" to "kotlin",
        "py" to "python",
    )

    fun setMarkdown(textView: TextView, markdown: String) {
        markwon.setMarkdown(textView, markdown.stripSpecials())
    }

    fun setMarkdown(textView: TextView, node: Node) {
        markwon.setParsedMarkdown(textView, markwon.render(node))
    }

    fun setMarkdown(textView: TextView, spanned: Spanned) {
        markwon.setParsedMarkdown(textView, spanned)
    }

    @WorkerThread
    fun parse(markdown: String): Node = markwon.parse(
        markdown.stripSpecials().sanitizeLanguageBlocks()
    )

    fun reduce(node: Node): List<Node> = reducer.reduce(node)

    @WorkerThread
    private fun String.sanitizeLanguageBlocks(): String {
        var sanitizedMarkdown = this
        languageBlockMapping.forEach { (actual, expected) ->
            sanitizedMarkdown = sanitizedMarkdown.replace(
                oldValue = "```$actual\r",
                newValue = "```$expected\r",
            )
        }
        return sanitizedMarkdown
    }

    override fun render(node: Node): Spanned = markwon.render(node)
}
