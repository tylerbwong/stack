package me.tylerbwong.stack.data.network.service

import android.support.annotation.StringDef
import io.reactivex.Single
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestionService {

    companion object {

        // query params
        private const val SITE_PARAM = "site"
        private const val SORT_PARAM = "sort"
        private const val ORDER_PARAM = "order"
        private const val PAGE_SIZE_PARAM = "pagesize"
        private const val PAGE_PARAM = "page"
        private const val FILTER_PARAM = "filter"
        private const val TAGGED_PARAM = "tagged"

        // query param values
        internal const val DESC = "desc"
        internal const val ASC = "asc"
        internal const val CREATION = "creation"
        internal const val ACTIVITY = "activity"
        internal const val VOTES = "votes"
        internal const val HOT = "hot"
        internal const val WEEK = "week"
        internal const val MONTH = "month"

        @StringDef(DESC, ASC)
        annotation class Order

        @StringDef(CREATION, ACTIVITY, VOTES, HOT, WEEK, MONTH)
        annotation class Sort

        // defaults
        private const val DEFAULT_SITE = "stackoverflow"
        private const val DEFAULT_FILTER = "!-N4vhDh8TGjM*h(2reCz3exHc6q)hWsdi"
        private const val DEFAULT_SORT = CREATION
        private const val DEFAULT_ORDER = DESC
        private const val DEFAULT_PAGE_SIZE = 50
        private const val DEFAULT_PAGE = 1

        // detail
        private const val DETAIL_FILTER = "!3r.zRmD4l6rHdTgXfBOo(qq6rg_D3I7uaTO)p123.RRrNwbbeBOKxJp8dch552I"
    }

    @GET("questions")
    fun getQuestions(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER
    ): Single<Response<Question>>

    @GET("questions")
    fun getQuestionsByTags(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(TAGGED_PARAM) tags: String
    ): Single<Response<Question>>

    @GET("questions/{id}")
    fun getQuestionDetails(
            @Path("id") questionId: Int,
            @Query(FILTER_PARAM) filter: String = DETAIL_FILTER
    ): Single<Response<Question>>

    @GET("questions/{id}/answers")
    fun getQuestionAnswers(
            @Path("id") questionId: Int,
            @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DETAIL_FILTER
    ): Single<Response<Answer>>
}