package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import me.tylerbwong.markdown.compose.builder.buildMarkdown
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode

internal object HeaderContentVisitor : Visitor {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder, content: String) {
        node.children
            .dropWhile { it.type == MarkdownTokenTypes.WHITE_SPACE }
            .forEach { builder.buildMarkdown(it, content) }
    }
}
