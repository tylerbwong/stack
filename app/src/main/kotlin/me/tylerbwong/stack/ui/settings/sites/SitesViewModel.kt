package me.tylerbwong.stack.ui.settings.sites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.LogOutResult
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent

class SitesViewModel @ViewModelInject constructor(
    private val siteRepository: SiteRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val logOutState: LiveData<SiteLogOutResult>
        get() = mutableLogOutState
    private val mutableLogOutState = SingleLiveEvent<SiteLogOutResult>()

    internal val sites: LiveData<List<Site>>
        get() = mutableSites
    private val mutableSites = MutableLiveData<List<Site>>()

    internal val searchQuery: LiveData<String>
        get() = mutableSearchQuery
    private val mutableSearchQuery = MutableLiveData<String>()

    internal val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated.value ?: false

    internal var currentQuery: String? = null
    private var searchCatalog: List<Site> = emptyList()

    internal fun changeSite(site: String) = siteRepository.changeSite(site)

    internal fun fetchSites(query: String? = null) {
        launchRequest { siteRepository.fetchSitesIfNecessary() }
        mutableSearchQuery.value = query ?: ""
        if (query.isNullOrEmpty()) {
            streamRequest(siteRepository.getSites()) { results ->
                searchCatalog = results.map { it.toSite() }
                mutableSites.value = searchCatalog
            }
        } else {
            currentQuery = query
            mutableSites.value = searchCatalog.filter { site ->
                val containsName = site.name
                    .replace(whitespaceRegex, "")
                    .contains(query, ignoreCase = true)
                val containsAudience = site.audience
                    .replace(whitespaceRegex, "")
                    .contains(query, ignoreCase = true)
                containsName || containsAudience
            }
        }
    }

    internal fun logOut(siteParameter: String) {
        launchRequest {
            when (authRepository.logOut()) {
                is LogOutResult.LogOutError -> mutableSnackbar.value = Unit
                is LogOutResult.LogOutSuccess -> {
                    mutableSnackbar.value = null
                    mutableLogOutState.value = SiteLogOutResult.SiteLogOutSuccess(siteParameter)
                }
            }
        }
    }

    companion object {
        private val whitespaceRegex = "\\s".toRegex()
    }
}

sealed class SiteLogOutResult {
    class SiteLogOutSuccess(val siteParameter: String) : SiteLogOutResult()
}
