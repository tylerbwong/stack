package me.tylerbwong.markdown.compose.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.ast.ASTNode

object UnorderedListItemVisitor : Visitor {

    private const val BULLET_CHAR = '•'

    override fun accept(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        builder.withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
            append("    $BULLET_CHAR ")
        }
    }
}
