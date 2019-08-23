package me.tylerbwong.stack.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.preference.PreferenceManager
import me.tylerbwong.stack.R

object ThemeManager {

    private var isDarkModeEnabled: Boolean = false

    private const val DARK_MODE_ENABLED = "dark_mode_enabled"

    fun init(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        isDarkModeEnabled = preferences.getBoolean(DARK_MODE_ENABLED, false)
    }

    fun injectTheme(activity: Activity) {
        if (isDarkModeEnabled) {
            activity.setTheme(R.style.AppTheme_Primary_Base_Dark)
            removeLightStatusBarIfSupported(activity)
            removeLightNavigationBarIfSupported(activity)
        } else {
            setLightStatusBarIfSupported(activity)
            setLightNavigationBarIfSupported(activity)
        }
    }

    private fun setLightStatusBarIfSupported(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    private fun removeLightStatusBarIfSupported(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    private fun setLightNavigationBarIfSupported(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    private fun removeLightNavigationBarIfSupported(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    fun toggleTheme(context: Context) {
        isDarkModeEnabled = !isDarkModeEnabled
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(DARK_MODE_ENABLED, isDarkModeEnabled)
                .apply()
    }

    fun resolveThemeAttribute(context: Context, @AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
