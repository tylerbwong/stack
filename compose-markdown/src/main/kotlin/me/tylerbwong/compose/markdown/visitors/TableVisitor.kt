@file:Suppress("MagicNumber")
package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes

internal object TableVisitor : Visitor {

    override val acceptedTypes = setOf(GFMElementTypes.TABLE)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        val inlineContentKey = node.toString()
        inlineTextContent[inlineContentKey] = InlineTextContent(
            placeholder = Placeholder(250.sp, 250.sp, PlaceholderVerticalAlign.AboveBaseline)
        ) {
            // TODO draw table here
            Text(text = node.getTextInNode(content).toString())
        }
        builder.appendInlineContent(inlineContentKey, inlineContentKey)
    }
}
