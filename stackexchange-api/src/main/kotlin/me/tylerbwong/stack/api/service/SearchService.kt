package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.ACTIVITY
import me.tylerbwong.stack.api.model.ORDER_PARAM
import me.tylerbwong.stack.api.model.Order
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.SORT_PARAM
import me.tylerbwong.stack.api.model.Sort
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
        @Query(SORT_PARAM) @Sort sort: String = ACTIVITY,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = QuestionService.DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
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
