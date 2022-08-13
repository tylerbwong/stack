package me.tylerbwong.stack.ui.utils

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.data.auth.AuthStore

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

internal fun Context.showLogOutDialog(onLogOutClicked: () -> Unit) {
    showDialog {
        setTitle(R.string.log_out_title)
        setMessage(R.string.log_out_message)
        setPositiveButton(R.string.log_out) { _, _ -> onLogOutClicked() }
        setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
    }
}

internal fun Context.showLogInDialog() {
    showDialog {
        setTitle(R.string.log_in_title)
        setPositiveButton(R.string.log_in) { _, _ -> launchUrl(AuthStore.authUrl) }
        setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
    }
}

internal fun Context.showRegisterOnSiteDialog(
    site: Site,
    siteUrl: String,
    @StringRes titleResId: Int = R.string.register_on_site,
) {
    showDialog {
        setTitle(getString(titleResId, site.name))
        setMessage(R.string.register_on_site_message)
        setPositiveButton(R.string.register) { _, _ -> launchUrl(siteUrl) }
        setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
    }
}
