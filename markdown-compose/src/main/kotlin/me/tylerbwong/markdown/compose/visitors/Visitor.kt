package me.tylerbwong.markdown.compose.visitors

import androidx.compose.ui.text.AnnotatedString
import org.intellij.markdown.ast.ASTNode

internal interface Visitor {
    fun accept(node: ASTNode, builder: AnnotatedString.Builder, content: String)
}
