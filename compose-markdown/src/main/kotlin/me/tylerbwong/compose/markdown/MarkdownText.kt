package me.tylerbwong.compose.markdown

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import me.tylerbwong.compose.markdown.builder.toMarkdownTextContent

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    onLinkClicked: (link: String) -> Unit = {}
) {
    val content by remember(markdown) { mutableStateOf(markdown) }
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val (
        annotatedString,
        inlineContent,
        linkPositions
    ) = content.toMarkdownTextContent(MaterialTheme.typography)
    val pointerInput = Modifier.pointerInput(null) {
        detectTapGestures { offset ->
            layoutResult?.let { result ->
                val offsetForPosition = result.getOffsetForPosition(offset)
                val linkKey = linkPositions.keys.firstOrNull { linkRange ->
                    offsetForPosition in linkRange
                }
                linkPositions[linkKey]?.let { link ->
                    onLinkClicked(link)
                }
            }
        }
    }
    Text(
        text = annotatedString,
        modifier = modifier.then(pointerInput),
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        inlineContent = inlineContent,
        onTextLayout = { layoutResult = it }
    )
}
