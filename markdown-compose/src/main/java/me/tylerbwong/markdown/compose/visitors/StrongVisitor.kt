package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import me.tylerbwong.markdown.compose.buildMarkdown
import org.intellij.markdown.ast.ASTNode

class StrongVisitor(fullMarkdownContent: String) : Visitor(fullMarkdownContent) {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            node.children
                // Drop "**" tokens before and after
                .drop(2)
                .dropLast(2)
                .forEach { buildMarkdown(it, fullMarkdownContent) }
        }
    }
}
