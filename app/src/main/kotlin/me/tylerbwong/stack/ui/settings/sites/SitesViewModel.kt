package me.tylerbwong.stack.ui.settings.sites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SitesViewModel @Inject constructor(
    private val siteRepository: SiteRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val associatedSites: LiveData<List<Site>>
        get() = mutableAssociatedSites
    private val mutableAssociatedSites = MutableLiveData<List<Site>>()

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
        mutableSearchQuery.value = query ?: ""
        if (query.isNullOrEmpty()) {
            currentQuery = null
            streamRequest(siteRepository.getSites()) { results ->
                searchCatalog = results.map { it.toSite() }
                mutableAssociatedSites.value = searchCatalog
                    .applyFilter(filter)
                    .filter { it.isUserRegistered }
                mutableSites.value = searchCatalog
                    .applyFilter(filter)
                    .filterNot { it.isUserRegistered }
            }
        } else {
            currentQuery = query
            mutableAssociatedSites.value = searchCatalog
                .applyFilter(filter)
                .applySearch(query)
                .filter { it.isUserRegistered }
            mutableSites.value = searchCatalog
                .applyFilter(filter)
                .applySearch(query)
                .filterNot { it.isUserRegistered }
        }
    }

    internal fun forceFetchSites() {
        launchRequest {
            siteRepository.fetchSitesIfNecessary(forceUpdate = true)
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

    private fun List<Site>.applySearch(query: String): List<Site> {
        return filter {
            val containsName = it.name.contains(query, ignoreCase = true)
            val containsAudience = it.audience.contains(query, ignoreCase = true)
            containsName || containsAudience
        }
    }
}
