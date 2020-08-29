package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import me.tylerbwong.markdown.compose.builder.buildMarkdown
import org.intellij.markdown.ast.ASTNode

internal class EmphasisVisitor(content: String) : Visitor(content) {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
            node.children
                // Drop "_" token before and after
                .drop(1)
                .dropLast(1)
                .forEach { buildMarkdown(it, content) }
        }
    }
}
