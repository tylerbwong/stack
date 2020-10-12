package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Typography
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode

internal class HeaderVisitor(private val typography: Typography) : Visitor {

    override val acceptedTypes = setOf(
        MarkdownElementTypes.SETEXT_1,
        MarkdownElementTypes.SETEXT_2,
        MarkdownElementTypes.ATX_1,
        MarkdownElementTypes.ATX_2,
        MarkdownElementTypes.ATX_3,
        MarkdownElementTypes.ATX_4,
        MarkdownElementTypes.ATX_5,
        MarkdownElementTypes.ATX_6
    )

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        val textStyle = resolveHeaderTextStyle(node.type)
        builder.withStyle(textStyle.toSpanStyle()) {
            node.children
                .drop(1) // Drop the header token
                .dropWhile { it.type == MarkdownTokenTypes.WHITE_SPACE }
                .forEach { continuation(it, content) }
        }
    }

    private fun resolveHeaderTextStyle(nodeType: IElementType): TextStyle {
        return with(typography) {
            when (nodeType) {
                MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.ATX_1 -> h1
                MarkdownElementTypes.SETEXT_2, MarkdownElementTypes.ATX_2 -> h2
                MarkdownElementTypes.ATX_3 -> h3
                MarkdownElementTypes.ATX_4 -> h4
                MarkdownElementTypes.ATX_5 -> h5
                MarkdownElementTypes.ATX_6 -> h6
                else -> TextStyle.Default
            }
        }
    }
}
