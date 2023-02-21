package me.tylerbwong.stack.ui.theme

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors

object ThemeManager {

    private const val CURRENT_MODE = "current_mode"

    val Context.isNightModeEnabled: Boolean
        get() = configurationMode == Configuration.UI_MODE_NIGHT_YES

    private val Context.configurationMode: Int
        get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    @AppCompatDelegate.NightMode
    internal val Context.delegateMode: Int
        get() = PreferenceManager.getDefaultSharedPreferences(this).getInt(
            CURRENT_MODE,
            MODE_NIGHT_FOLLOW_SYSTEM,
        )

    fun init(application: Application) {
        AppCompatDelegate.setDefaultNightMode(application.delegateMode)
        DynamicColors.applyToActivitiesIfAvailable(application)
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
        val decorView = activity.window.decorView
        var flags = decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        decorView.systemUiVisibility = flags
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    private fun removeLightStatusBarIfSupported(activity: Activity) {
        val decorView = activity.window.decorView
        var flags = decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        decorView.systemUiVisibility = flags
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    private fun setLightNavigationBarIfSupported(activity: Activity) {
        val decorView = activity.window.decorView
        var flags = decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        decorView.systemUiVisibility = flags
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    private fun removeLightNavigationBarIfSupported(activity: Activity) {
        val decorView = activity.window.decorView
        var flags = decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        decorView.systemUiVisibility = flags
    }

    fun toggleTheme(context: Context, newMode: Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt(CURRENT_MODE, newMode)
            .apply()
        AppCompatDelegate.setDefaultNightMode(newMode)
    }
}
