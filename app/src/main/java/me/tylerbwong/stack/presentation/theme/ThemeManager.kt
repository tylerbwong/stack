package me.tylerbwong.stack.presentation.theme

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.support.annotation.AttrRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.View
import me.tylerbwong.stack.R
import timber.log.Timber

object ThemeManager {

    private var isDarkModeEnabled: Boolean = false

    private const val DARK_MODE_ENABLED = "DARK_mode_enabled"

    fun init(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        isDarkModeEnabled = preferences.getBoolean(DARK_MODE_ENABLED, false)
    }

    fun injectTheme(activity: Activity) {
        if (isDarkModeEnabled) {
            activity.setTheme(R.style.AppTheme_Primary_Base_Dark)
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

    fun themeViews(vararg views: View) = views.forEach {
        it.applyTheme()
    }

    private fun View.applyTheme() = when (this) {
        is Toolbar -> {
            /* TODO Apply [Toolbar] theme */
        }
        is FloatingActionButton -> {
            /* TODO Apply [FloatingActionButton] theme */
        }
        else -> Timber.e("Could not find theme for view.")
    }
}
