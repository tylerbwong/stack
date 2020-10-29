package me.tylerbwong.stack.data.site

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.api.service.DEFAULT_SITE
import me.tylerbwong.stack.data.di.SiteSharedPreferences
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SiteStore @Inject constructor(
    @SiteSharedPreferences private val preferences: SharedPreferences
) {
    var site: String
        @Synchronized
        get() = deepLinkSite ?: preferences.getString(SITE_KEY, defaultSite) ?: defaultSite
        set(value) {
            preferences.edit().putString(SITE_KEY, value).apply()
            mutableSiteLiveData.value = value
        }

    /**
     * Used to determine the current site for the deep link session only.
     */
    internal var deepLinkSite: String? = null
        @Synchronized
        get
        set(value) {
            if (value != site) field = value
        }

    private val mutableSiteLiveData = MutableLiveData(site)

    val siteLiveData: LiveData<String> = mutableSiteLiveData

    companion object {
        internal const val SITE_PREFERENCES = "site_preferences"
        internal val defaultSite: String
            get() {
                val language = Locale.getDefault().language
                return if (language in supportedOtherLanguages) {
                    "$language.$DEFAULT_SITE"
                } else {
                    DEFAULT_SITE
                }
            }
        private val supportedOtherLanguages = setOf("es", "ja", "pt", "ru")
        private const val SITE_KEY = "site"
    }
}
