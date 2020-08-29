package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import me.tylerbwong.markdown.compose.buildMarkdown
import org.intellij.markdown.ast.ASTNode

class EmptyVisitor(fullMarkdownContent: String) : Visitor(fullMarkdownContent) {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder) {
        node.children.forEach { builder.buildMarkdown(it, fullMarkdownContent) }
    }
}
