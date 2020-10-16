package me.tylerbwong.compose.markdown

import androidx.compose.foundation.Text
import androidx.compose.foundation.currentTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import me.tylerbwong.compose.markdown.builder.toMarkdownTextContent

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Inherit,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    onLinkClicked: (link: String) -> Unit = {},
    style: TextStyle = currentTextStyle()
) {
    val content by remember(markdown) { mutableStateOf(markdown) }
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val (
        annotatedString,
        inlineContent,
        linkPositions
    ) = content.toMarkdownTextContent(MaterialTheme.typography)
    val tapGestureFilter = Modifier.tapGestureFilter { offset ->
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
    Text(
        text = annotatedString,
        modifier = modifier.then(tapGestureFilter),
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = { layoutResult = it },
        style = style
    )
}
