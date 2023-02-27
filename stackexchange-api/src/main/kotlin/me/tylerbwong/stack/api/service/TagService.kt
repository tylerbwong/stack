package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.ORDER_PARAM
import me.tylerbwong.stack.api.model.Order
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.SORT_PARAM
import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.api.model.TagPreference
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TagService {

    @GET("tags")
    suspend fun getPopularTags(
        @Query(INNAME_PARAM) inname: String? = null,
        @Query(SORT_PARAM) sort: String = TAGS_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = TAGS_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = TAGS_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Tag>

    @GET("tags/{tags}/info")
    suspend fun getTagsInfo(
        @Path(TAGS_PARAM) tags: String,
        @Query(SORT_PARAM) sort: String = TAGS_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = TAGS_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = TAGS_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Tag>

    @GET("/me/tag-preferences")
    suspend fun getTagPreferences(
        @Query(PAGE_SIZE_PARAM) pageSize: Int = TAGS_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = TAG_PREFERENCES_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<TagPreference>

    companion object {
        private const val TAGS_PARAM = "tags"
        private const val INNAME_PARAM = "inname"
        private const val TAGS_SORT = "popular"
        private const val TAGS_PAGE_SIZE = 30
        private const val TAGS_FILTER = "!0XrIP(5mCa0R7ys-I*Wa36*Jm"
        private const val TAG_PREFERENCES_FILTER = "!9eQ3TP1n-"
    }
}
