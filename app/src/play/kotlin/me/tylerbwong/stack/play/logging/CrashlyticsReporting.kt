package me.tylerbwong.stack.play.logging

import android.content.SharedPreferences
import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.tylerbwong.stack.data.logging.CrashReporting

class CrashlyticsReporting(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val preferences: SharedPreferences,
) : CrashReporting {

    override var isCrashReportingEnabled: Boolean
        get() = preferences.getBoolean(CRASH_REPORTING_ENABLED_PREF, true)
        set(value) {
            preferences.edit().putBoolean(CRASH_REPORTING_ENABLED_PREF, value).apply()
            firebaseCrashlytics.setCrashlyticsCollectionEnabled(value)
        }

    override fun initialize() {
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(isCrashReportingEnabled)
    }

    companion object {
        private const val CRASH_REPORTING_ENABLED_PREF = "crash_reporting_enabled"
    }
}
