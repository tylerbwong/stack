package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Typography
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
                MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.ATX_1 -> headlineLarge
                MarkdownElementTypes.SETEXT_2, MarkdownElementTypes.ATX_2 -> headlineMedium
                MarkdownElementTypes.ATX_3 -> headlineSmall
                MarkdownElementTypes.ATX_4 -> bodyLarge
                MarkdownElementTypes.ATX_5 -> bodyMedium
                MarkdownElementTypes.ATX_6 -> bodySmall
                else -> TextStyle.Default
            }
        }
    }
}
