package me.tylerbwong.stack.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.util.TypedValue
import androidx.annotation.AttrRes

inline fun <reified T : Any> Context.systemService(
    systemService: String
): T? = getSystemService(systemService) as? T

inline fun <reified T : Context> Context.ofType(): T? {
    var currentContext: Context? = this
    do {
        if (currentContext is T) {
            return currentContext
        }

        currentContext = (currentContext as? ContextWrapper)?.baseContext
    } while (currentContext != null)
    return null
}

fun Context.resolveThemeAttribute(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun Context.copyToClipboard(label: String, text: String): Boolean {
    val clipboardManager = systemService<ClipboardManager>(Context.CLIPBOARD_SERVICE)
    return if (clipboardManager != null) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
        true
    } else {
        false
    }
}
