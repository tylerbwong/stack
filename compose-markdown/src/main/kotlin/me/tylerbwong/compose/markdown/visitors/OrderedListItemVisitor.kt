package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

internal object OrderedListItemVisitor : Visitor {

    private const val ORDER_SUFFIX = '.'

    override val acceptedTypes = setOf(MarkdownTokenTypes.LIST_NUMBER)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        builder.withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
            val rawListMarker = node.getTextInNode(content).trim()
            val formattedListMarker = if (!rawListMarker.endsWith(ORDER_SUFFIX)) {
                "${rawListMarker.dropLast(1)}$ORDER_SUFFIX" // Handle 1) case
            } else {
                rawListMarker
            }
            append("    $formattedListMarker ")
        }
    }
}
