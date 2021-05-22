package me.tylerbwong.stack.ui.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import me.tylerbwong.stack.R

object ThemeManager {

    val Context.isNightModeEnabled: Boolean
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

    const val CURRENT_MODE = "current_mode"

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

    @Suppress("deprecation") // TODO Update to Android R APIs
    private fun setLightStatusBarIfSupported(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            decorView.systemUiVisibility = flags
        }
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    private fun removeLightStatusBarIfSupported(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            decorView.systemUiVisibility = flags
        }
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    private fun setLightNavigationBarIfSupported(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            decorView.systemUiVisibility = flags
        } else {
            window.navigationBarColor = ContextCompat.getColor(activity, R.color.black)
        }
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    private fun removeLightNavigationBarIfSupported(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            decorView.systemUiVisibility = flags
        }
    }

    fun toggleTheme(context: Context, newMode: Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt(CURRENT_MODE, newMode)
            .apply()
        AppCompatDelegate.setDefaultNightMode(newMode)
    }
}
