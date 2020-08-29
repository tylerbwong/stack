package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import me.tylerbwong.markdown.compose.builder.buildMarkdown
import org.intellij.markdown.ast.ASTNode

internal object CodeSpanVisitor : Visitor {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder, content: String) {
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
