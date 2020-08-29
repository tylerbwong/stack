package me.tylerbwong.markdown.compose

import androidx.compose.foundation.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import me.tylerbwong.markdown.compose.builder.toMarkdownAnnotatedString

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val content by remember(markdown) { mutableStateOf(markdown) }
    Text(text = content.toMarkdownAnnotatedString(), modifier = modifier)
}
