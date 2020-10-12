package me.tylerbwong.compose.markdown.visitors

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode

internal typealias Continuation = AnnotatedString.Builder.(
    node: ASTNode,
    content: String
) -> AnnotatedString.Builder

internal interface Visitor {

    val acceptedTypes: Set<IElementType>

    fun visit(
        node: ASTNode,
        builder: AnnotatedString.Builder,
        content: String,
        inlineTextContent: MutableMap<String, InlineTextContent>,
        linkPositions: MutableMap<IntRange, String>,
        continuation: Continuation
    )

    fun shouldVisit(node: ASTNode) = node.type in acceptedTypes
}
