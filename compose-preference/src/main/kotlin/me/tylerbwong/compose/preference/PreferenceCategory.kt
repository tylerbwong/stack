@file:Suppress("FunctionNaming", "MagicNumber")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun PreferenceScope.PreferenceCategory(
    name: String,
    labelColor: @Composable () -> Color = { MaterialTheme.colors.secondary },
    content: PreferenceScope.() -> Unit
) {
    item {
        Text(
            text = name,
            modifier = Modifier
                .padding(
                    start = 74.dp,
                    top = 16.dp,
                    bottom = 8.dp,
                ),
            color = labelColor(),
            fontWeight = FontWeight.Bold,
        )
    }
    content()
    item { Divider() }
}
