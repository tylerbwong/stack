package me.tylerbwong.stack.data.network.service

import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.model.Site
import me.tylerbwong.stack.data.network.ServiceProvider
import retrofit2.http.GET
import retrofit2.http.Query

interface SiteService {

    @GET("sites")
    suspend fun getSites(
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_SITE_PAGE_SIZE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_SITE_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Site>

    companion object {
        private const val DEFAULT_SITE_PAGE_SIZE = 500 // Get all sites
        private const val DEFAULT_SITE_FILTER = "!FmOlfAlxIKzxseAE9fn4_UPMXe"
    }
}
