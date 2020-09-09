@file:Suppress("MagicNumber")
package me.tylerbwong.markdown.compose.visitors

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

object CodeBlockVisitor : Visitor {

    override val acceptedTypes = setOf(
        MarkdownElementTypes.CODE_FENCE,
        MarkdownElementTypes.CODE_BLOCK
    )

    override fun accept(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    ) {
        val codeBlockKey = node.toString()
        val codeBlockContentBuilder = AnnotatedString.Builder()
        node.children
            // Drop "```" token before and after
            .drop(2)
            .dropLast(2)
            .forEach { codeBlockContentBuilder.continuation(it, content) }
        val codeBlockContent = codeBlockContentBuilder.toAnnotatedString()

        inlineTextContent[codeBlockKey] = InlineTextContent(
            placeholder = Placeholder(
                width = 400.sp,
                height = 72.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.LightGray.copy(alpha = 0.5f)
            ) {
                Text(
                    text = codeBlockContent,
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
        builder.appendInlineContent(codeBlockKey, codeBlockContent.text)
    }
}
