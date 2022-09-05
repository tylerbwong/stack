package me.tylerbwong.stack.ui.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode
import me.tylerbwong.stack.ui.utils.showDialog

val nightModeOptions = mapOf(
    R.string.theme_light to MODE_NIGHT_NO,
    R.string.theme_dark to MODE_NIGHT_YES,
    R.string.theme_system_default to MODE_NIGHT_FOLLOW_SYSTEM,
)

fun Context.showThemeChooserDialog(onSelected: () -> Unit) {
    showDialog {
        setTitle(R.string.theme_title)
        setSingleChoiceItems(
            nightModeOptions.keys.map { getString(it) }.toTypedArray(),
            nightModeOptions.values.indexOf(delegateMode)
        ) { dialog, which ->
            ThemeManager.toggleTheme(
                this@showThemeChooserDialog,
                nightModeOptions.values.toList()[which]
            )
            onSelected()
            dialog.dismiss()
        }
    }
}
