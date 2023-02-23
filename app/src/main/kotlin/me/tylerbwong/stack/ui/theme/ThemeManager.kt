package me.tylerbwong.stack.ui.theme

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.view.WindowInsetsControllerCompat
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
        val isNightModeEnabled = activity.isNightModeEnabled
        val window = activity.window
        val decorView = window.decorView
        with(WindowInsetsControllerCompat(window, decorView)) {
            isAppearanceLightStatusBars = !isNightModeEnabled
            isAppearanceLightNavigationBars = !isNightModeEnabled
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
