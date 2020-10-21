package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

internal object LinkVisitor : Visitor {

    override val acceptedTypes = setOf(MarkdownElementTypes.INLINE_LINK)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        val linkDestination = node.children
            .firstOrNull { it.type == MarkdownElementTypes.LINK_DESTINATION }
            ?.getTextInNode(content) ?: return
        val linkTextNode = node.children
            .firstOrNull { it.type == MarkdownElementTypes.LINK_TEXT } ?: return
        val linkTextStart = builder.length
        builder.withStyle(
            SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        ) {
            linkTextNode.children
                .drop(1) // Drop "[]"
                .dropLast(1)
                .forEach { continuation(it, content) }
        }
        val linkTextEnd = builder.length
        linkPositions[linkTextStart..linkTextEnd] = linkDestination.toString()
    }
}
