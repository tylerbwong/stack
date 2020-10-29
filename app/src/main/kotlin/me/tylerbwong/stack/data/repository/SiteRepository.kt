package me.tylerbwong.stack.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import me.tylerbwong.stack.api.service.SiteService
import me.tylerbwong.stack.data.persistence.dao.SiteDao
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.data.toSiteEntity
import javax.inject.Inject

class SiteRepository @Inject constructor(
    private val siteDao: SiteDao,
    private val siteService: SiteService,
    private val siteStore: SiteStore
) {

    internal val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal val site: String
        get() = siteStore.site

    internal suspend fun getCurrentSite() = siteDao.getSite(site)?.toSite()

    internal fun changeSite(site: String) {
        siteStore.site = site
    }

    internal fun getSites() = siteDao.getSites()

    internal suspend fun fetchSitesIfNecessary() {
        if (getCurrentSite() == null) {
            flow { emit(siteService.getSites().items) }
                .retry(NUM_SITE_SYNC_RETRIES)
                .collect { sites -> siteDao.insert(sites.map { it.toSiteEntity() }) }
        }
    }

    companion object {
        internal const val NUM_SITE_SYNC_RETRIES = 3L
    }
}
