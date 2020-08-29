package me.tylerbwong.markdown.compose.builder

import androidx.compose.ui.text.AnnotatedString
import me.tylerbwong.markdown.compose.visitors.CodeSpanVisitor
import me.tylerbwong.markdown.compose.visitors.EmphasisVisitor
import me.tylerbwong.markdown.compose.visitors.EmptyVisitor
import me.tylerbwong.markdown.compose.visitors.HeaderVisitor
import me.tylerbwong.markdown.compose.visitors.StrikethroughVisitor
import me.tylerbwong.markdown.compose.visitors.StrongVisitor
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

fun String.toMarkdownAnnotatedString(): AnnotatedString {
    val flavour = GFMFlavourDescriptor()
    val rootNode = MarkdownParser(flavour).buildMarkdownTreeFromString(this)
    val annotatedStringBuilder = AnnotatedString.Builder()
    return annotatedStringBuilder.buildMarkdown(rootNode, this).toAnnotatedString()
}

internal fun AnnotatedString.Builder.buildMarkdown(
    node: ASTNode,
    content: String
): AnnotatedString.Builder {
    when (node.type) {
        MarkdownElementTypes.MARKDOWN_FILE, MarkdownElementTypes.PARAGRAPH,
        MarkdownTokenTypes.ATX_CONTENT -> EmptyVisitor(content).accept(node, this)
        MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.ATX_1, MarkdownElementTypes.SETEXT_2,
        MarkdownElementTypes.ATX_2, MarkdownElementTypes.ATX_3, MarkdownElementTypes.ATX_4,
        MarkdownElementTypes.ATX_5, MarkdownElementTypes.ATX_6 -> {
            HeaderVisitor(content, node.type).accept(node, this)
        }
        MarkdownElementTypes.STRONG -> StrongVisitor(content).accept(node, this)
        MarkdownElementTypes.EMPH -> EmphasisVisitor(content).accept(node, this)
        MarkdownElementTypes.CODE_SPAN -> CodeSpanVisitor(content).accept(node, this)
        GFMElementTypes.STRIKETHROUGH -> StrikethroughVisitor(content).accept(node, this)
        else -> append(text = node.getTextInNode(content).toString())
    }

    return this
}
