package me.tylerbwong.stack.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.api.service.SiteService
import me.tylerbwong.stack.api.service.UserService
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.persistence.dao.SiteDao
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.data.site.normalizeSite
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.data.toSiteEntity
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class SiteRepository @Inject constructor(
    private val siteDao: SiteDao,
    private val siteService: SiteService,
    private val userService: UserService,
    private val siteStore: SiteStore,
    private val authRepository: AuthRepository,
) {
    internal val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal val site: String
        get() = siteStore.site

    internal suspend fun getCurrentSite() = siteDao.getSite(site)?.toSite()

    internal fun changeSite(site: String) {
        siteStore.site = site
    }

    internal fun buildSiteJoinUrl(site: Site): String {
        val httpUrl = site.url.toHttpUrl()
        return httpUrl.newBuilder()
            .addEncodedPathSegments(SiteStore.USER_JOIN_PATH)
            .build()
            .toString()
    }

    internal fun getSites() = siteDao.getSites()

    internal suspend fun fetchSitesIfNecessary(forceUpdate: Boolean = false) {
        if (forceUpdate || getCurrentSite() == null) {
            flow { emit(siteService.getSites().items) }
                .retry(NUM_SITE_SYNC_RETRIES)
                .flowOn(Dispatchers.IO)
                .collect { sites ->
                    // This will be empty if the user is not logged in
                    val associatedParameters = if (authRepository.isAuthenticated) {
                        userService.getCurrentUserNetworkUsers()
                            .items
                            .map { it.siteUrl.normalizeSite() }
                    } else {
                        emptyList()
                    }
                    // User will always be registered under every meta site as well
                    val metaAssociatedParameters = associatedParameters.map { "meta.$it" } +
                            associatedParameters.map { "$it.meta" }
                    val parameters = associatedParameters + metaAssociatedParameters
                    siteDao.insert(sites.map { it.toSiteEntity(parameters) })
                }
        }
    }

    companion object {
        internal const val NUM_SITE_SYNC_RETRIES = 3L
    }
}
