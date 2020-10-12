package me.tylerbwong.compose.markdown.builder

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Typography
import androidx.compose.ui.text.AnnotatedString
import me.tylerbwong.compose.markdown.visitors.BlockQuoteVisitor
import me.tylerbwong.compose.markdown.visitors.CheckboxVisitor
import me.tylerbwong.compose.markdown.visitors.CodeBlockVisitor
import me.tylerbwong.compose.markdown.visitors.CodeSpanVisitor
import me.tylerbwong.compose.markdown.visitors.EmphasisVisitor
import me.tylerbwong.compose.markdown.visitors.EmptyVisitor
import me.tylerbwong.compose.markdown.visitors.HeaderContentVisitor
import me.tylerbwong.compose.markdown.visitors.HeaderVisitor
import me.tylerbwong.compose.markdown.visitors.ImageVisitor
import me.tylerbwong.compose.markdown.visitors.LinkVisitor
import me.tylerbwong.compose.markdown.visitors.OrderedListItemVisitor
import me.tylerbwong.compose.markdown.visitors.StrikethroughVisitor
import me.tylerbwong.compose.markdown.visitors.StrongVisitor
import me.tylerbwong.compose.markdown.visitors.TableVisitor
import me.tylerbwong.compose.markdown.visitors.UnorderedListItemVisitor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

internal fun String.toAstNode(
    flavourDescriptor: MarkdownFlavourDescriptor = GFMFlavourDescriptor()
): ASTNode = MarkdownParser(flavourDescriptor).buildMarkdownTreeFromString(this)

internal fun String.toMarkdownTextContent(typography: Typography): MarkdownTextContent {
    val rootNode = toAstNode()
    val inlineTextContent = mutableMapOf<String, InlineTextContent>()
    val linkPositions = mutableMapOf<IntRange, String>()
    val visitors = listOf(
        EmptyVisitor,
        HeaderVisitor(typography),
        HeaderContentVisitor,
        StrongVisitor,
        EmphasisVisitor,
        CodeSpanVisitor,
        CodeBlockVisitor,
        ImageVisitor,
        LinkVisitor,
        StrikethroughVisitor,
        UnorderedListItemVisitor,
        OrderedListItemVisitor,
        BlockQuoteVisitor,
        TableVisitor,
        CheckboxVisitor
    )

    fun AnnotatedString.Builder.buildMarkdown(
        node: ASTNode,
        content: String
    ): AnnotatedString.Builder {
        val visitor = visitors.firstOrNull { it.shouldVisit(node) }
        if (visitor != null) {
            visitor.visit(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                AnnotatedString.Builder::buildMarkdown
            )
        } else {
            append(text = node.getTextInNode(content).toString())
        }
        return this
    }

    val annotatedString = AnnotatedString.Builder()
        .buildMarkdown(rootNode, this)
        .toAnnotatedString()
    return MarkdownTextContent(annotatedString, inlineTextContent, linkPositions)
}
