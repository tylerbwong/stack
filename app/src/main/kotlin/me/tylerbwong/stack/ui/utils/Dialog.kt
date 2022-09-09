package me.tylerbwong.stack.ui.utils

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.data.auth.AuthStore
import com.google.android.material.R as MaterialR

inline fun Context.showDialog(
    showOnCreate: Boolean = true,
    defaultStyle: Int = MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered,
    config: MaterialAlertDialogBuilder.() -> MaterialAlertDialogBuilder
): Dialog {
    return MaterialAlertDialogBuilder(this, defaultStyle)
        .config()
        .create()
        .also { if (showOnCreate) it.show() }
}

internal fun Context.showLogOutDialog(onLogOutClicked: () -> Unit) {
    showDialog {
        setIcon(R.drawable.ic_baseline_logout)
        setTitle(R.string.log_out_title)
        setMessage(R.string.log_out_message)
        setPositiveButton(R.string.log_out) { _, _ -> onLogOutClicked() }
        setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
    }
}

internal fun Context.showLogInDialog() {
    showDialog {
        setIcon(R.drawable.ic_account_circle)
        setTitle(R.string.log_in_title)
        setMessage(R.string.log_in_message)
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
        setIcon(R.drawable.ic_baseline_face)
        setTitle(getString(titleResId, site.name))
        setMessage(R.string.register_on_site_message)
        setPositiveButton(R.string.register) { _, _ -> launchUrl(siteUrl) }
        setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
    }
}
