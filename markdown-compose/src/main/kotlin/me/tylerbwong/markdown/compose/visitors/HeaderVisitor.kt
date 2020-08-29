@file:Suppress("MagicNumber")
package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import me.tylerbwong.markdown.compose.builder.buildMarkdown
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode

internal object HeaderVisitor : Visitor {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder, content: String) {
        builder.withStyle(SpanStyle(fontSize = resolveHeaderTextSize(node.type))) {
            node.children
                .drop(1) // Drop the header token
                .dropWhile { it.type == MarkdownTokenTypes.WHITE_SPACE }
                .forEach { buildMarkdown(it, content) }
        }
    }

    private fun resolveHeaderTextSize(nodeType: IElementType) = when (nodeType) {
        MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.ATX_1 -> 32.sp
        MarkdownElementTypes.SETEXT_2, MarkdownElementTypes.ATX_2 -> 28.sp
        MarkdownElementTypes.ATX_3 -> 24.sp
        MarkdownElementTypes.ATX_4 -> 20.sp
        MarkdownElementTypes.ATX_5 -> 18.sp
        MarkdownElementTypes.ATX_6 -> 16.sp
        else -> TextUnit.Inherit
    }
}
