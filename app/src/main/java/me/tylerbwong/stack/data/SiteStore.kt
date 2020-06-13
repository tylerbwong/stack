package me.tylerbwong.stack.data

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.network.service.DEFAULT_SITE
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SiteStore @Inject constructor(
    @Named("siteSharedPreferences") private val preferences: SharedPreferences
) {
    var site: String
        @Synchronized
        get() = deepLinkSite ?: preferences.getString(SITE_KEY, DEFAULT_SITE) ?: DEFAULT_SITE
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
        private const val SITE_KEY = "site"
    }
}
