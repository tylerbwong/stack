package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import me.tylerbwong.markdown.compose.parser.buildMarkdown
import org.intellij.markdown.ast.ASTNode

class StrongVisitor(content: String) : Visitor(content) {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            node.children
                // Drop "**" tokens before and after
                .drop(2)
                .dropLast(2)
                .forEach { buildMarkdown(it, content) }
        }
    }
}
