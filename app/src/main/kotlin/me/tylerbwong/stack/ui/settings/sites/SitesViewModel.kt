package me.tylerbwong.stack.ui.settings.sites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.LogOutResult
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SitesViewModel @Inject constructor(
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

    internal val filter: LiveData<SiteFilter>
        get() = mutableFilter
    private val mutableFilter = MutableLiveData<SiteFilter>()

    internal val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated

    internal var currentQuery: String? = null
    internal var currentFilter: SiteFilter = SiteFilter.All
    private var searchCatalog: List<Site> = emptyList()

    internal fun changeSite(site: String) = siteRepository.changeSite(site)

    internal fun fetchSites(query: String? = currentQuery, filter: SiteFilter = currentFilter) {
        currentFilter = filter
        mutableFilter.value = filter
        launchRequest { siteRepository.fetchSitesIfNecessary() }
        mutableSearchQuery.value = query ?: ""
        if (query.isNullOrEmpty()) {
            currentQuery = null
            streamRequest(siteRepository.getSites()) { results ->
                searchCatalog = results.map { it.toSite() }
                mutableSites.value = searchCatalog.applyFilter(filter)
            }
        } else {
            currentQuery = query
            mutableSites.value = searchCatalog.applyFilter(filter).filter { site ->
                val containsName = site.name.contains(query, ignoreCase = true)
                val containsAudience = site.audience.contains(query, ignoreCase = true)
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

    private fun List<Site>.applyFilter(filter: SiteFilter): List<Site> {
        return filter {
            when (filter) {
                SiteFilter.All -> true
                SiteFilter.Main -> !it.parameter.contains("meta", ignoreCase = true)
                SiteFilter.Meta -> it.parameter.contains("meta", ignoreCase = true)
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
