package me.tylerbwong.stack.ui.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import me.tylerbwong.stack.R

object ThemeManager {

    private val Context.isNightModeEnabled: Boolean
        get() = configurationMode == Configuration.UI_MODE_NIGHT_YES

    private val Context.configurationMode: Int
        get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    @AppCompatDelegate.NightMode
    internal val Context.delegateMode: Int
        get() = PreferenceManager.getDefaultSharedPreferences(this).getInt(
                CURRENT_MODE,
                defaultDelegateMode
        )

    @AppCompatDelegate.NightMode
    internal val defaultDelegateMode: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            MODE_NIGHT_AUTO_BATTERY
        }

    private const val CURRENT_MODE = "current_mode"

    fun init(context: Context) {
        AppCompatDelegate.setDefaultNightMode(context.delegateMode)
    }

    fun injectTheme(activity: Activity) {
        if (activity.isNightModeEnabled) {
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
        } else {
            activity.window.navigationBarColor = ContextCompat.getColor(activity, R.color.black)
        }
    }

    private fun removeLightNavigationBarIfSupported(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    fun toggleTheme(context: Context, newMode: Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(CURRENT_MODE, newMode)
                .apply()
        AppCompatDelegate.setDefaultNightMode(newMode)
    }

    fun resolveThemeAttribute(context: Context, @AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
