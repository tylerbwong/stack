package me.tylerbwong.markdown.compose.builder

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString

internal data class MarkdownTextContent(
    val annotatedString: AnnotatedString,
    val inlineTextContent: Map<String, InlineTextContent>
)
