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

    var createQuestionEnabled: Boolean
        get() = preferences.getBoolean(CREATE_QUESTION, false)
        set(value) = preferences.edit().putBoolean(CREATE_QUESTION, value).apply()

    companion object {
        internal const val EXPERIMENTAL_SHARED_PREFS = "experimental_shared_prefs"
        const val MARKDOWN_SYNTAX_HIGHLIGHT = "markdown_syntax_highlight"
        const val CREATE_QUESTION = "create_question"
    }
}
