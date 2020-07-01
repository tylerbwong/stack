package me.tylerbwong.stack.data

import android.content.Context
import leakcanary.LeakCanary
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.ui.ApplicationWrapper

object Leaks {
    private const val DEBUG_SHARED_PREFS = "debug_shared_prefs"
    private const val LEAK_CANARY_OPTION = "leak_canary_option"

    var isLeakCanaryEnabled: Boolean
        get() {
            val preferences = ApplicationWrapper.context.getSharedPreferences(
                DEBUG_SHARED_PREFS,
                Context.MODE_PRIVATE
            )
            return BuildConfig.DEBUG && preferences.getBoolean(LEAK_CANARY_OPTION, false)
        }
        set(value) {
            val preferences = ApplicationWrapper.context.getSharedPreferences(
                DEBUG_SHARED_PREFS,
                Context.MODE_PRIVATE
            )
            preferences.edit().putBoolean(LEAK_CANARY_OPTION, value).apply()
        }

    fun initialize(isEnabled: Boolean = isLeakCanaryEnabled) {
        isLeakCanaryEnabled = isEnabled
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = isEnabled)
        LeakCanary.showLeakDisplayActivityLauncherIcon(showLauncherIcon = isEnabled)
    }
}
