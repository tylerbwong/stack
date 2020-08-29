package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import me.tylerbwong.markdown.compose.builder.buildMarkdown
import org.intellij.markdown.ast.ASTNode

internal class EmptyVisitor(content: String) : Visitor(content) {

    override fun accept(node: ASTNode, builder: AnnotatedString.Builder) {
        node.children.forEach { builder.buildMarkdown(it, content) }
    }
}
