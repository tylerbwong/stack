@file:Suppress("MagicNumber")
package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Checkbox
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes

internal object CheckboxVisitor : Visitor {

    override val acceptedTypes = setOf(GFMTokenTypes.CHECK_BOX)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        val checkboxKey = node.toString()
        val nodeText = node.getTextInNode(content).toString()
        inlineTextContent[checkboxKey] = InlineTextContent(
            placeholder = Placeholder(
                width = 32.sp,
                height = 24.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Row {
                Checkbox(
                    checked = node.isChecked(nodeText),
                    onCheckedChange = {}
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
        builder.appendInlineContent(checkboxKey, nodeText)
    }

    private fun ASTNode.isChecked(nodeText: String): Boolean =
        type == GFMTokenTypes.CHECK_BOX && "[x]" in nodeText
}
