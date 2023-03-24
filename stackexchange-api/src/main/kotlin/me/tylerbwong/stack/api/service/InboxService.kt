package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.InboxItem
import me.tylerbwong.stack.api.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InboxService {

    @GET("inbox")
    suspend fun getInbox(
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = INBOX_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<InboxItem>

    companion object {
        private const val INBOX_FILTER = "!FmdZhk)FozDf*_(motx6Ztdnab"
    }
}
