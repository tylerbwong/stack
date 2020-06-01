package me.tylerbwong.stack.data.repository

import androidx.lifecycle.LiveData
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.network.service.SiteService
import me.tylerbwong.stack.data.persistence.dao.SiteDao
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.data.toSiteEntity

class SiteRepository(
    private val siteDao: SiteDao,
    private val siteService: SiteService,
    private val siteStore: SiteStore
) {

    internal val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal val site: String
        get() = siteStore.site

    internal suspend fun getCurrentSite() = siteDao.getSite(site).toSite()

    internal fun changeSite(site: String) {
        siteStore.site = site
    }

    internal fun getSites() = siteDao.getSites()

    internal suspend fun fetchSitesIfNecessary() {
        if (siteDao.getCount() == 0) {
            val sites = siteService.getSites().items
            siteDao.insert(sites.map { it.toSiteEntity() })
        }
    }
}
