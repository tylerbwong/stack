package me.tylerbwong.stack.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import me.tylerbwong.stack.R

private const val LINK_LABEL = "stack_link"

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
