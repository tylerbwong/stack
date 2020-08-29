package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import org.intellij.markdown.ast.ASTNode

abstract class Visitor(protected val content: String) {
    abstract fun accept(node: ASTNode, builder: AnnotatedString.Builder)
}
