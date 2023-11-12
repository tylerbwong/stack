package me.tylerbwong.stack.play.logging

import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.google.firebase.analytics.logEvent
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.site.SiteStore

class FirebaseLogger(
    private val analytics: FirebaseAnalytics,
    private val authStore: AuthStore,
    private val siteStore: SiteStore,
    private val preferences: SharedPreferences,
) : Logger {
    override var isLoggingEnabled: Boolean
        get() = preferences.getBoolean(LOGGING_ENABLED_PREF, true)
        set(value) = preferences.edit().putBoolean(LOGGING_ENABLED_PREF, value).apply()

    override fun logEvent(eventName: String, vararg params: Pair<String, String>) {
        if (isLoggingEnabled) {
            analytics.logEvent(eventName) {
                params.forEach { param(it.first, it.second) }
                appendBaseParams()
            }
        }
    }

    private fun ParametersBuilder.appendBaseParams() {
        param(BASE_PARAM_AUTHENTICATED, authStore.isAuthenticatedLiveData.value.toString())
        param(BASE_PARAM_SITE, siteStore.site)
    }

    companion object {
        private const val LOGGING_ENABLED_PREF = "logging_enabled"
        private const val BASE_PARAM_AUTHENTICATED = "is_authenticated"
        private const val BASE_PARAM_SITE = "site"
    }
}
