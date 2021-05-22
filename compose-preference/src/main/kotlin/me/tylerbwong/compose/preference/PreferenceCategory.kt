@file:Suppress("FunctionNaming", "MagicNumber")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun PreferenceScope.PreferenceCategory(
    header: @Composable () -> Unit,
    divider: (@Composable () -> Unit)? = { Divider() },
    content: PreferenceScope.() -> Unit,
) {
    item {
        Box(
            modifier = Modifier
                .padding(
                    start = 74.dp,
                    top = 20.dp,
                    bottom = 8.dp,
                ),
        ) {
            ProvideTextStyle(
                value = TextStyle(
                    color = MaterialTheme.colors.secondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            ) { header() }
        }
    }
    content()
    divider?.let { item { it() } }
}
