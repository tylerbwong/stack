package me.tylerbwong.stack.ui.utils.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView
import me.tylerbwong.stack.latex.LatexTextView

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
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
            LatexTextView(it).apply {
                if (fontSize != TextUnit.Unspecified) {
                    this.textSize = textSize
                }
            }
        },
        modifier = modifier,
        update = { it.setLatex(markdown) },
    )
}
