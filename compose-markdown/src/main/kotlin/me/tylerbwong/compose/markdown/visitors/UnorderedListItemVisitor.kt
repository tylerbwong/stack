package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes

internal object UnorderedListItemVisitor : Visitor {

    private const val BULLET_CHAR = '•'

    override val acceptedTypes = setOf(MarkdownTokenTypes.LIST_BULLET)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        val isCheckboxItem = node.parent?.children?.any {
            it.type == GFMTokenTypes.CHECK_BOX
        } ?: false
        // Only append bullet if this isn't a Checkbox item
        if (!isCheckboxItem) {
            builder.withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                append("    $BULLET_CHAR ")
            }
        }
    }
}
