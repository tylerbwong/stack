package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Article
import me.tylerbwong.stack.api.model.ORDER_PARAM
import me.tylerbwong.stack.api.model.Order
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.SORT_PARAM
import me.tylerbwong.stack.api.model.Sort
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleService {

    @GET("articles")
    suspend fun getArticles(
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = ARTICLE_FEED_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY,
    ): Response<Article>

    @GET("articles/{id}")
    suspend fun getArticleDetails(
        @Path("id") id: Int,
        @Query(FILTER_PARAM) filter: String = ARTICLE_DETAILS_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Article>

    companion object {
        private const val ARTICLE_FEED_FILTER = "!Fc6b6ofEc7uJRx9sto9vvle2kz"
        private const val ARTICLE_DETAILS_FILTER = "!szxkzhQVWO(uj0YS--Po1KjfkZukp)f"
    }
}
