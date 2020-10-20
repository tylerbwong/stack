package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import me.tylerbwong.stack.R
import timber.log.Timber

private const val STABLE_PACKAGE = "com.android.chrome"
private const val BETA_PACKAGE = "com.chrome.beta"
private const val DEV_PACKAGE = "com.chrome.dev"
private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"

fun Context.launchUrl(url: String) {
    val uri = Uri.parse(url)
    val packageName = getPackageNameToUse(this, uri)
    if (packageName != null) {
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
        customTabsIntent.intent.`package` = packageName
        customTabsIntent.launchUrl(this, uri)
    } else {
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

@Suppress("ComplexMethod")
private fun getPackageNameToUse(context: Context, uri: Uri): String? {
    val packageManager = context.packageManager
    val activityIntent = Intent(Intent.ACTION_VIEW, uri)
    val defaultViewHandlerInfo = packageManager.resolveActivity(activityIntent, 0)
    val defaultViewHandlerPackageName = defaultViewHandlerInfo?.activityInfo?.packageName

    val resolvedActivityList = packageManager.queryIntentActivities(activityIntent, 0)
    val packagesSupportingCustomTabs = mutableListOf<String>()
    resolvedActivityList.forEach {
        val serviceIntent = Intent()
        serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
        serviceIntent.`package` = it.activityInfo.packageName
        if (packageManager.resolveService(serviceIntent, 0) != null) {
            packagesSupportingCustomTabs.add(it.activityInfo.packageName)
        }
    }

    return when {
        packagesSupportingCustomTabs.isEmpty() -> null
        packagesSupportingCustomTabs.size == 1 -> packagesSupportingCustomTabs[0]
        !TextUtils.isEmpty(defaultViewHandlerPackageName) &&
                !hasSpecializedHandlerIntents(context, activityIntent) &&
                packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName) ->
            defaultViewHandlerPackageName
        packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
        packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> BETA_PACKAGE
        packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> DEV_PACKAGE
        packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> LOCAL_PACKAGE
        else -> {
            Timber.e("Could not resolve package for custom tabs")
            null
        }
    }
}

@Suppress("ComplexMethod")
private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
    try {
        val pm = context.packageManager
        val handlers = pm.queryIntentActivities(
            intent,
            PackageManager.GET_RESOLVED_FILTER
        )
        if (handlers.size == 0) {
            return false
        }
        for (resolveInfo in handlers) {
            val filter = resolveInfo.filter ?: continue
            if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
            if (resolveInfo.activityInfo == null) continue
            return true
        }
    } catch (e: RuntimeException) {
        Timber.e("Runtime exception while getting specialized handlers")
    }

    return false
}
