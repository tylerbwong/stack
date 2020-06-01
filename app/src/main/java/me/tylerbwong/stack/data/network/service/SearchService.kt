package me.tylerbwong.stack.data.network.service

import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.model.ORDER_PARAM
import me.tylerbwong.stack.data.model.Order
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.RELEVANCE
import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.model.SORT_PARAM
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @Suppress("LongParameterList")
    @GET("search/advanced")
    suspend fun search(
        @Query(QUERY_PARAM) query: String,
        @Query(ACCEPTED_PARAM) isAccepted: Boolean? = null,
        @Query(ANSWERS_PARAM) minNumAnswers: Int? = null,
        @Query(BODY_PARAM) bodyContains: String? = null,
        @Query(CLOSED_PARAM) isClosed: Boolean? = null,
        @Query(TAGGED_PARAM) tags: String? = null,
        @Query(TITLE_PARAM) titleContains: String? = null,
        @Query(SITE_PARAM) site: String = SiteStore.site,
        @Query(SORT_PARAM) @Sort sort: String = RELEVANCE,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = QuestionService.DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Question>

    companion object {
        private const val QUERY_PARAM = "q"
        private const val ACCEPTED_PARAM = "accepted"
        private const val ANSWERS_PARAM = "answers"
        private const val BODY_PARAM = "body"
        private const val CLOSED_PARAM = "closed"
        private const val TAGGED_PARAM = "tagged"
        private const val TITLE_PARAM = "title"
    }
}
