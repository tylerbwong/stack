package me.tylerbwong.compose.markdown.builder

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString

internal data class MarkdownTextContent(
    val annotatedString: AnnotatedString,
    val inlineTextContent: Map<String, InlineTextContent>,
    val linkPositions: Map<IntRange, String>
)
