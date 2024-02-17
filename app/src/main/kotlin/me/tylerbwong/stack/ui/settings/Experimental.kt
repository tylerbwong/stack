package me.tylerbwong.stack.ui.settings

import android.content.SharedPreferences
import me.tylerbwong.stack.data.di.ExperimentalSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Experimental @Inject constructor(
    @ExperimentalSharedPreferences private val preferences: SharedPreferences
) {
    var syntaxHighlightingEnabled: Boolean
        get() = preferences.getBoolean(MARKDOWN_SYNTAX_HIGHLIGHT, false)
        set(value) = preferences.edit().putBoolean(MARKDOWN_SYNTAX_HIGHLIGHT, value).apply()

    var siteDrawerEnabled: Boolean
        get() = preferences.getBoolean(SITE_DRAWER, false)
        set(value) = preferences.edit().putBoolean(SITE_DRAWER, value).apply()

    companion object {
        internal const val EXPERIMENTAL_SHARED_PREFS = "experimental_shared_prefs"
        private const val MARKDOWN_SYNTAX_HIGHLIGHT = "markdown_syntax_highlight"
        private const val SITE_DRAWER = "site_drawer"
    }
}
