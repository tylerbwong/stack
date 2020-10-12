package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

internal object EmphasisVisitor : Visitor {

    override val acceptedTypes = setOf(MarkdownElementTypes.EMPH)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
            node.children
                // Drop "_" token before and after
                .drop(1)
                .dropLast(1)
                .forEach { continuation(it, content) }
        }
    }
}
