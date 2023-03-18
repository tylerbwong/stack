package me.tylerbwong.stack.ui.utils.compose

import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView
import me.tylerbwong.stack.markdown.MarkdownTextView

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    isBold: Boolean = false,
) {
    val textSize = if (fontSize != TextUnit.Unspecified) {
        with(LocalDensity.current) {
            fontSize.toDp().value
        }
    } else {
        0f
    }
    AndroidView(
        factory = {
            MarkdownTextView(it).apply {
                if (fontSize != TextUnit.Unspecified) {
                    this.textSize = textSize
                }
                if (isBold) {
                    setTypeface(typeface, Typeface.BOLD)
                } else {
                    setTypeface(typeface, Typeface.NORMAL)
                }
            }
        },
        modifier = modifier,
        update = { it.setMarkdown(markdown) },
    )
}
