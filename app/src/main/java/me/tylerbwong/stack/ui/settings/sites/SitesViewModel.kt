package me.tylerbwong.stack.ui.settings.sites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.Site
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.ui.BaseViewModel

class SitesViewModel(
    private val siteRepository: SiteRepository
) : BaseViewModel() {

    internal val sites: LiveData<List<Site>>
        get() = mutableSites
    private val mutableSites = MutableLiveData<List<Site>>()

    internal var currentQuery: String? = null

    internal fun fetchSites() {
        launchRequest { siteRepository.fetchSitesIfNecessary() }
        streamRequest(siteRepository.getSites()) { results ->
            mutableSites.value = results.map { it.toSite() }
        }
    }
}
