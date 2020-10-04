package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode

internal object HeaderContentVisitor : Visitor {

    override val acceptedTypes = setOf(MarkdownTokenTypes.ATX_CONTENT)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        node.children
            .dropWhile { it.type == MarkdownTokenTypes.WHITE_SPACE }
            .forEach { builder.continuation(it, content) }
    }
}
