package me.tylerbwong.markdown.compose.builder

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Typography
import androidx.compose.ui.text.AnnotatedString
import me.tylerbwong.markdown.compose.visitors.BlockQuoteVisitor
import me.tylerbwong.markdown.compose.visitors.CodeBlockVisitor
import me.tylerbwong.markdown.compose.visitors.CodeSpanVisitor
import me.tylerbwong.markdown.compose.visitors.EmphasisVisitor
import me.tylerbwong.markdown.compose.visitors.EmptyVisitor
import me.tylerbwong.markdown.compose.visitors.HeaderContentVisitor
import me.tylerbwong.markdown.compose.visitors.HeaderVisitor
import me.tylerbwong.markdown.compose.visitors.ImageVisitor
import me.tylerbwong.markdown.compose.visitors.LinkVisitor
import me.tylerbwong.markdown.compose.visitors.OrderedListItemVisitor
import me.tylerbwong.markdown.compose.visitors.StrikethroughVisitor
import me.tylerbwong.markdown.compose.visitors.StrongVisitor
import me.tylerbwong.markdown.compose.visitors.TableVisitor
import me.tylerbwong.markdown.compose.visitors.UnorderedListItemVisitor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

internal fun String.toMarkdownTextContent(typography: Typography): MarkdownTextContent {
    val flavour = GFMFlavourDescriptor()
    val rootNode = MarkdownParser(flavour).buildMarkdownTreeFromString(this)
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
        TableVisitor
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
