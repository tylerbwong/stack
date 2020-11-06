package me.tylerbwong.stack.ui.utils

import android.app.Dialog
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.tylerbwong.stack.R

inline fun Context.showDialog(
    showOnCreate: Boolean = true,
    config: MaterialAlertDialogBuilder.() -> MaterialAlertDialogBuilder
): Dialog {
    return MaterialAlertDialogBuilder(this)
        .setBackground(ContextCompat.getDrawable(this, R.drawable.default_dialog_bg))
        .config()
        .create()
        .also { if (showOnCreate) it.show() }
}
