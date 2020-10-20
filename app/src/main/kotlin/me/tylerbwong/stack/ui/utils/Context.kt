package me.tylerbwong.stack.ui.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import me.tylerbwong.stack.R

private const val LINK_LABEL = "stack_link"

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

fun Context.launchUrl(url: String) {
    try {
        val themeColor = resolveThemeAttribute(R.attr.viewBackgroundColor)
        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setNavigationBarColor(themeColor)
                    .setToolbarColor(themeColor)
                    .setSecondaryToolbarColor(themeColor)
                    .build()
            )
            .build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    } catch (ex: ActivityNotFoundException) {
        // TODO Resolve to internal Web View
        val isCopied = copyToClipboard(LINK_LABEL, url)
        if (isCopied) {
            Toast.makeText(this, R.string.no_browser_found, Toast.LENGTH_LONG).show()
        }
    }
}
