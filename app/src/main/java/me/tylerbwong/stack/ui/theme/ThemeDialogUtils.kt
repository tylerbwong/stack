package me.tylerbwong.stack.ui.theme

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode

val nightModeOptions = mapOf(
    R.string.theme_light to MODE_NIGHT_NO,
    R.string.theme_dark to MODE_NIGHT_YES,
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        R.string.theme_system_default to MODE_NIGHT_FOLLOW_SYSTEM
    } else {
        R.string.theme_battery_saver to MODE_NIGHT_AUTO_BATTERY
    }
)

fun Context.showThemeChooserDialog() {
    MaterialAlertDialogBuilder(this)
        .setBackground(ContextCompat.getDrawable(this, R.drawable.default_dialog_bg))
        .setTitle(R.string.theme_title)
        .setSingleChoiceItems(
            nightModeOptions.keys.map { getString(it) }.toTypedArray(),
            nightModeOptions.values.indexOf(delegateMode)
        ) { dialog, which ->
            ThemeManager.toggleTheme(
                this,
                nightModeOptions.values.toList()[which]
            )
            dialog.dismiss()
        }
        .create()
        .show()
}
