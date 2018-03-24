package me.tylerbwong.stack.presentation.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.customtabs.CustomTabsService
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import me.tylerbwong.stack.R
import timber.log.Timber

private const val STABLE_PACKAGE = "com.android.chrome"
private const val BETA_PACKAGE = "com.chrome.beta"
private const val DEV_PACKAGE = "com.chrome.dev"
private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"

private var packageName: String? = null

fun launchCustomTab(context: Context, url: String) {
    val packageName = getPackageNameToUse(context)
    val customTabsIntent = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
            ))
            .build()
    customTabsIntent.intent.`package` = packageName
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

private fun getPackageNameToUse(context: Context): String? {
    if (packageName != null) return packageName

    val pm = context.packageManager
    // Get default VIEW intent handler.
    val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
    val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)
    var defaultViewHandlerPackageName: String? = null
    if (defaultViewHandlerInfo != null) {
        defaultViewHandlerPackageName = defaultViewHandlerInfo!!.activityInfo.packageName
    }

    // Get all apps that can handle VIEW intents.
    val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
    val packagesSupportingCustomTabs = ArrayList<String>()
    for (info in resolvedActivityList) {
        val serviceIntent = Intent()
        serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
        serviceIntent.`package` = info.activityInfo.packageName
        if (pm.resolveService(serviceIntent, 0) != null) {
            packagesSupportingCustomTabs.add(info.activityInfo.packageName)
        }
    }

    // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
    // and service calls.
    if (packagesSupportingCustomTabs.isEmpty()) {
        packageName = null
    }
    else if (packagesSupportingCustomTabs.size == 1) {
        packageName = packagesSupportingCustomTabs.get(0)
    }
    else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
            && !hasSpecializedHandlerIntents(context, activityIntent)
            && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
        packageName = defaultViewHandlerPackageName
    }
    else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
        packageName = STABLE_PACKAGE
    }
    else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
        packageName = BETA_PACKAGE
    }
    else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
        packageName = DEV_PACKAGE
    }
    else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
        packageName = LOCAL_PACKAGE
    }
    return packageName
}

private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
    try {
        val pm = context.packageManager
        val handlers = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER)
        if (handlers == null || handlers.size == 0) {
            return false
        }
        for (resolveInfo in handlers) {
            val filter = resolveInfo.filter ?: continue
            if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
            if (resolveInfo.activityInfo == null) continue
            return true
        }
    }
    catch (e: RuntimeException) {
        Timber.e("Runtime exception while getting specialized handlers")
    }

    return false
}