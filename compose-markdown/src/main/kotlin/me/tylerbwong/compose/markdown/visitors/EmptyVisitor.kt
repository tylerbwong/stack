package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

internal object EmptyVisitor : Visitor {

    override val acceptedTypes = setOf(
        MarkdownElementTypes.MARKDOWN_FILE,
        MarkdownElementTypes.PARAGRAPH,
        MarkdownElementTypes.UNORDERED_LIST,
        MarkdownElementTypes.ORDERED_LIST,
        MarkdownElementTypes.LIST_ITEM
    )

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        node.children.forEach { builder.continuation(it, content) }
    }
}
