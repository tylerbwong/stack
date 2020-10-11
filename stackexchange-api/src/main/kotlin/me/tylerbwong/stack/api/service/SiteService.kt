package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.Site
import retrofit2.http.GET
import retrofit2.http.Query

interface SiteService {

    @GET("sites")
    suspend fun getSites(
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_SITE_PAGE_SIZE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_SITE_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Site>

    companion object {
        private const val DEFAULT_SITE_PAGE_SIZE = 500 // Get all sites
        private const val DEFAULT_SITE_FILTER = "!Fn4IB7S7W()0gcwcuHN16kFhm9"
    }
}
