package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import me.tylerbwong.markdown.compose.parser.buildMarkdown
import org.intellij.markdown.ast.ASTNode

class CodeSpanVisitor(content: String) : Visitor(content) {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder) {
        builder.withStyle(
            SpanStyle(
                fontFamily = FontFamily.Monospace,
                background = Color.LightGray
            )
        ) {
            node.children
                // Drop "`" token before and after
                .drop(1)
                .dropLast(1)
                .forEach { buildMarkdown(it, content) }
        }
    }
}
