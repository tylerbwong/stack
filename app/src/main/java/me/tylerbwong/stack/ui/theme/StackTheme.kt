package me.tylerbwong.stack.ui.theme

import androidx.compose.Composable
import androidx.ui.core.ContextAmbient
import androidx.ui.material.MaterialTheme
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette
import androidx.ui.res.colorResource
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled

@Composable
fun StackTheme(content: @Composable () -> Unit) {
    val isNightModeEnabled = ContextAmbient.current.isNightModeEnabled
    MaterialTheme(
        colors = if (isNightModeEnabled) {
            darkColorPalette(
                surface = colorResource(R.color.viewBackgroundColor),
                background = colorResource(R.color.viewBackgroundColor)
            )
        } else {
            lightColorPalette(
                surface = colorResource(R.color.viewBackgroundColor),
                background = colorResource(R.color.viewBackgroundColor)
            )
        }
    ) {
        content()
    }
}
