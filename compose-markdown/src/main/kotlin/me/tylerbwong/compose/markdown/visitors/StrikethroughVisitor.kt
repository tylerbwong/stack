package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes

internal object StrikethroughVisitor : Visitor {

    override val acceptedTypes = setOf(GFMElementTypes.STRIKETHROUGH)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        builder.withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
            node.children
                // Drop "~~" token before and after
                .drop(2)
                .dropLast(2)
                .forEach { continuation(it, content) }
        }
    }
}
