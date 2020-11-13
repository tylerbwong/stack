@file:Suppress("MagicNumber")
package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

internal object BlockQuoteVisitor : Visitor {

    override val acceptedTypes = setOf(MarkdownElementTypes.BLOCK_QUOTE)

    override fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        val blockQuoteKey = node.toString()
        val blockQuoteContentBuilder = AnnotatedString.Builder()
        node.children
            // Drop ">" token
            .drop(1)
            .forEach { blockQuoteContentBuilder.continuation(it, content) }
        val blockQuoteContent = blockQuoteContentBuilder.toAnnotatedString()

        inlineTextContent[blockQuoteKey] = InlineTextContent(
            placeholder = Placeholder(
                width = 400.sp,
                height = 40.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Row(modifier = Modifier.fillMaxHeight()) {
                Box(
                    modifier = Modifier.fillMaxHeight()
                        .padding(start = 4.dp, end = 4.dp)
                        .width(3.dp)
                        .background(Color.LightGray, RoundedCornerShape(50))
                )
                Text(text = blockQuoteContent)
            }
        }
        builder.appendInlineContent(blockQuoteKey, blockQuoteContent.text)
    }
}
