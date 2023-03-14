package me.tylerbwong.stack.data.preferences

import android.content.SharedPreferences
import me.tylerbwong.stack.data.di.StackSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @StackSharedPreferences private val preferences: SharedPreferences,
) {
    var shouldGoToMainOnBackFromDeepLink: Boolean
        get() = preferences.getBoolean(DEEP_LINK_BACK_BEHAVIOR, false)
        set(value) = preferences.edit().putBoolean(DEEP_LINK_BACK_BEHAVIOR, value).apply()

    companion object {
        private const val DEEP_LINK_BACK_BEHAVIOR = "deep_link_back_behavior"
    }
}
