package me.tylerbwong.stack.ui.utils.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.tylerbwong.stack.R

/**
 * This denotes all the available toolbar item options.
 * @param token The text that should be inserted. "|" represents the cursor position when inserted.
 * @param cursorPosition Precomputed token.indexOf("|").
 */
enum class ToolbarItem(
    val token: String,
    val cursorPosition: Int,
    internal val icon: @Composable () -> Unit,
) {
    BOLD(
        token = "**|**",
        cursorPosition = 2,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatBold,
                contentDescription = Icons.Default.FormatBold.name,
            )
        },
    ),
    ITALIC(
        token = "_|_",
        cursorPosition = 1,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatItalic,
                contentDescription = Icons.Default.FormatItalic.name,
            )
        },
    ),
    LINK(
        token = "[|](url)",
        cursorPosition = 1,
        icon = {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = Icons.Default.Link.name,
            )
        },
    ),
    INLINE_CODE(
        token = "`|`",
        cursorPosition = 1,
        icon = {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = Icons.Default.Code.name,
            )
        },
    ),
    CODE_BLOCK(
        token = "```\n|\n```",
        cursorPosition = 4,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_code_block),
                contentDescription = "Code block",
            )
        }
    ),
    BLOCK_QUOTE(
        token = "> |",
        cursorPosition = 2,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = Icons.Default.FormatQuote.name,
            )
        },
    ),
    BULLETED_LIST(
        token = "* |",
        cursorPosition = 2,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatListBulleted,
                contentDescription = Icons.Default.FormatListBulleted.name,
            )
        },
    ),
    NUMBERED_LIST(
        token = "1. |",
        cursorPosition = 3,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatListNumbered,
                contentDescription = Icons.Default.FormatListNumbered.name,
            )
        },
    ),
    H1(
        token = "# |",
        cursorPosition = 2,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h1),
                contentDescription = toString(),
            )
        }
    ),
    H2(
        token = "## |",
        cursorPosition = 3,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h2),
                contentDescription = toString(),
            )
        }
    ),
    H3(
        token = "### |",
        cursorPosition = 4,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h3),
                contentDescription = toString(),
            )
        }
    ),
    H4(
        token = "#### |",
        cursorPosition = 5,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h4),
                contentDescription = toString(),
            )
        }
    ),
    H5(
        token = "##### |",
        cursorPosition = 6,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h5),
                contentDescription = toString(),
            )
        }
    ),
    H6(
        token = "###### |",
        cursorPosition = 7,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h6),
                contentDescription = toString(),
            )
        }
    ),
}

@Composable
fun TextFormatToolbar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    LazyRow {
        itemsIndexed(ToolbarItem.values()) { index, item ->
            BarItem(
                onClick = {
                    val selection = value.selection
                    val selectionStart = selection.start
                    val selectionEnd = selection.end
                    val oldTextSelection = value.getSelectedText().text
                    val newTextWithFormat = AnnotatedString(
                        text = item.token.replace("|", oldTextSelection)
                    )
                    val textBeforeSelection = value.getTextBeforeSelection(selection.max)
                    val textAfterSelection = value.getTextAfterSelection(selection.max)
                    val textBeforeSelectionLength = textBeforeSelection.length
                    val newBodyText = textBeforeSelection + newTextWithFormat + textAfterSelection
                    onValueChange(
                        TextFieldValue(
                            text = newBodyText.text,
                            selection = if (selectionStart != selectionEnd) {
                                // Text was selected, put cursor after formatted text
                                TextRange(textBeforeSelectionLength + newTextWithFormat.length)
                            } else {
                                // Format was inserted at cursor, put cursor at cursorPosition
                                TextRange(textBeforeSelectionLength + item.cursorPosition)
                            }
                        )
                    )
                }
            ) {
                Box(modifier = Modifier.padding(4.dp)) {
                    item.icon()
                }
            }
            if (index < ToolbarItem.values().lastIndex) {
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
    }
}

@Composable
private fun BarItem(
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = Color.Transparent,
    ) { icon() }
}

@Preview
@Composable
private fun TextFormatToolbarPreview() {
    TextFormatToolbar(
        value = TextFieldValue(),
        onValueChange = {},
    )
}
