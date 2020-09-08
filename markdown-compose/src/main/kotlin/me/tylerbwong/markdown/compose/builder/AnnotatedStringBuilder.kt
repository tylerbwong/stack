@file:Suppress("LongMethod")

package me.tylerbwong.markdown.compose.builder

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
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
import me.tylerbwong.markdown.compose.visitors.UnorderedListItemVisitor
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

internal fun String.toMarkdownTextContent(): MarkdownTextContent {
    val flavour = GFMFlavourDescriptor()
    val rootNode = MarkdownParser(flavour).buildMarkdownTreeFromString(this)
    val inlineTextContent = mutableMapOf<String, InlineTextContent>()
    val linkPositions = mutableMapOf<IntRange, String>()

    fun AnnotatedString.Builder.buildMarkdown(
        node: ASTNode,
        content: String
    ): AnnotatedString.Builder {
        val continuation = AnnotatedString.Builder::buildMarkdown
        when (node.type) {
            MarkdownElementTypes.MARKDOWN_FILE, MarkdownElementTypes.PARAGRAPH,
            MarkdownElementTypes.UNORDERED_LIST, MarkdownElementTypes.ORDERED_LIST,
            MarkdownElementTypes.LIST_ITEM -> EmptyVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.ATX_1,
            MarkdownElementTypes.SETEXT_2, MarkdownElementTypes.ATX_2, MarkdownElementTypes.ATX_3,
            MarkdownElementTypes.ATX_4, MarkdownElementTypes.ATX_5, MarkdownElementTypes.ATX_6 ->
                HeaderVisitor.accept(
                    node,
                    this,
                    content,
                    inlineTextContent,
                    linkPositions,
                    continuation
                )
            MarkdownTokenTypes.ATX_CONTENT -> HeaderContentVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownElementTypes.STRONG -> StrongVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownElementTypes.EMPH -> EmphasisVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownElementTypes.CODE_SPAN -> CodeSpanVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownElementTypes.IMAGE -> ImageVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownElementTypes.INLINE_LINK -> LinkVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            GFMElementTypes.STRIKETHROUGH -> StrikethroughVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownTokenTypes.LIST_BULLET -> UnorderedListItemVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            MarkdownTokenTypes.LIST_NUMBER -> OrderedListItemVisitor.accept(
                node,
                this,
                content,
                inlineTextContent,
                linkPositions,
                continuation
            )
            else -> append(text = node.getTextInNode(content).toString())
        }
        return this
    }

    val annotatedString = AnnotatedString.Builder()
        .buildMarkdown(rootNode, this)
        .toAnnotatedString()
    return MarkdownTextContent(annotatedString, inlineTextContent, linkPositions)
}
