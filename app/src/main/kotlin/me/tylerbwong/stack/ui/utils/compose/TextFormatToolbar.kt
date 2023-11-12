@file:Suppress("MatchingDeclarationName")

package me.tylerbwong.stack.ui.utils.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Subscript
import androidx.compose.material.icons.filled.Superscript
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.R

/**
 * This denotes all the available toolbar item options.
 * @param token The text that should be inserted. "%" represents the cursor position when inserted.
 * @param cursorPosition Precomputed token.indexOf("%").
 * @param secondaryCursorPosition If the cursor should be placed at a position even after formatting
 * selected text, specify it here. `textOffset` allows for making the calculation based on any text
 * that was inserted into the token.
 * @param canFormatSelectedText If this format option can format highlighted text
 */
enum class ToolbarItem(
    val token: String,
    val cursorPosition: Int,
    val secondaryCursorPosition: ((textOffset: Int) -> Int)? = null,
    val canFormatSelectedText: Boolean = true,
    internal val icon: @Composable () -> Unit,
) {
    BOLD(
        token = "**%**",
        cursorPosition = 2,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatBold,
                contentDescription = Icons.Default.FormatBold.name,
            )
        },
    ),
    ITALIC(
        token = "_%_",
        cursorPosition = 1,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatItalic,
                contentDescription = Icons.Default.FormatItalic.name,
            )
        },
    ),
    STRIKETHROUGH(
        token = "~~%~~",
        cursorPosition = 2,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatStrikethrough,
                contentDescription = Icons.Default.FormatStrikethrough.name
            )
        }
    ),
    SUPERSCRIPT(
        token = "<sup>%</sup>",
        cursorPosition = 5,
        icon = {
            Icon(
                imageVector = Icons.Default.Superscript,
                contentDescription = Icons.Default.Superscript.name
            )
        }
    ),
    SUBSCRIPT(
        token = "<sub>%</sub>",
        cursorPosition = 5,
        icon = {
            Icon(
                imageVector = Icons.Default.Subscript,
                contentDescription = Icons.Default.Subscript.name
            )
        }
    ),
    LINK(
        token = "[%]()",
        cursorPosition = 1,
        secondaryCursorPosition = { it + 3 }, // placed at 1 + x + 2 after beginning
        icon = {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = Icons.Default.Link.name,
            )
        },
    ),
    INLINE_CODE(
        token = "`%`",
        cursorPosition = 1,
        icon = {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = Icons.Default.Code.name,
            )
        },
    ),
    CODE_BLOCK(
        token = "```\n%\n```",
        cursorPosition = 4,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_code_block),
                contentDescription = "Code block",
            )
        }
    ),
    BLOCK_QUOTE(
        token = "> %",
        cursorPosition = 2,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = Icons.Default.FormatQuote.name,
            )
        },
    ),
    TABLE(
        token = "| Column A | Column B |\n| -------- | -------- |\n| Cell 1   | Cell 2   |\n| Cell 3   | Cell 4   |\n%",
        cursorPosition = 96,
        canFormatSelectedText = false,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_table),
                contentDescription = toString(),
            )
        },
    ),
    BULLETED_LIST(
        token = "* %",
        cursorPosition = 2,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                contentDescription = Icons.AutoMirrored.Filled.FormatListBulleted.name,
            )
        },
    ),
    NUMBERED_LIST(
        token = "1. %",
        cursorPosition = 3,
        icon = {
            Icon(
                imageVector = Icons.Default.FormatListNumbered,
                contentDescription = Icons.Default.FormatListNumbered.name,
            )
        },
    ),
    HORIZONTAL_RULE(
        token = "---\n%",
        cursorPosition = 4,
        canFormatSelectedText = false,
        icon = {
            Icon(
                imageVector = Icons.Default.HorizontalRule,
                contentDescription = Icons.Default.HorizontalRule.name,
            )
        }
    ),
    H1(
        token = "# %",
        cursorPosition = 2,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h1),
                contentDescription = toString(),
            )
        }
    ),
    H2(
        token = "## %",
        cursorPosition = 3,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h2),
                contentDescription = toString(),
            )
        }
    ),
    H3(
        token = "### %",
        cursorPosition = 4,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h3),
                contentDescription = toString(),
            )
        }
    ),
    H4(
        token = "#### %",
        cursorPosition = 5,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h4),
                contentDescription = toString(),
            )
        }
    ),
    H5(
        token = "##### %",
        cursorPosition = 6,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_format_h5),
                contentDescription = toString(),
            )
        }
    ),
    H6(
        token = "###### %",
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
    val scope = rememberCoroutineScope()
    LazyRow(horizontalArrangement = spacedBy(2.dp)) {
        itemsIndexed(ToolbarItem.values()) { index, item ->
            if (index == 0) {
                Spacer(modifier = Modifier.width(4.dp))
            }
            BarItem(
                enabled = when {
                    value.selection.start != value.selection.end -> item.canFormatSelectedText
                    else -> true
                },
                onClick = {
                    scope.launch {
                        val newValue = withContext(Dispatchers.Default) {
                            val selection = value.selection
                            val selectionStart = selection.start
                            val selectionEnd = selection.end
                            val oldTextSelection = value.getSelectedText().text
                            val newTextWithFormat = AnnotatedString(
                                text = item.token.replace("%", oldTextSelection)
                            )
                            val textBeforeSelection =
                                value.annotatedString.subSequence(0, selection.start)
                            val textAfterSelection =
                                value.annotatedString.subSequence(selection.end, value.text.length)
                            val textBeforeSelectionLength = textBeforeSelection.length
                            val newBodyText =
                                textBeforeSelection + newTextWithFormat + textAfterSelection
                            TextFieldValue(
                                text = newBodyText.text,
                                selection = if (selectionStart != selectionEnd) {
                                    val secondaryCursorPosition = item.secondaryCursorPosition?.invoke(
                                        oldTextSelection.length
                                    )
                                    if (secondaryCursorPosition != null) {
                                        // This item supports placing the cursor at a secondary position
                                        TextRange(textBeforeSelectionLength + secondaryCursorPosition)
                                    } else {
                                        // Text was selected, put cursor after formatted text
                                        TextRange(textBeforeSelectionLength + newTextWithFormat.length)
                                    }
                                } else {
                                    // Format was inserted at cursor, put cursor at cursorPosition
                                    TextRange(textBeforeSelectionLength + item.cursorPosition)
                                }
                            )
                        }
                        onValueChange(newValue)
                    }
                }
            ) {
                Box(modifier = Modifier.padding(4.dp)) {
                    item.icon()
                }
            }
            if (index == ToolbarItem.values().lastIndex) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

private const val DISABLED_ALPHA = 0.38f

@Composable
private fun BarItem(
    enabled: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp))
            .alpha(if (enabled) 1f else DISABLED_ALPHA)
            .clickable(enabled) { onClick() },
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
