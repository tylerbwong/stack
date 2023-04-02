package me.tylerbwong.stack.play.logging

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.logEvent
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.site.SiteStore

class FirebaseLogger(
    private val analytics: FirebaseAnalytics,
    private val authStore: AuthStore,
    private val siteStore: SiteStore,
) : Logger {

    override fun logEvent(eventName: String, vararg params: Pair<String, String>) {
        analytics.logEvent(eventName) {
            params.forEach { param(it.first, it.second) }
            appendBaseParams()
        }
    }

    private fun ParametersBuilder.appendBaseParams() {
        param(BASE_PARAM_AUTHENTICATED, authStore.isAuthenticatedLiveData.value.toString())
        param(BASE_PARAM_SITE, siteStore.site)
    }

    companion object {
        private const val BASE_PARAM_AUTHENTICATED = "is_authenticated"
        private const val BASE_PARAM_SITE = "site"
    }
}
