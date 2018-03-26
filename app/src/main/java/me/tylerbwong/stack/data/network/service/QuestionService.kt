package me.tylerbwong.stack.data.network.service

import io.reactivex.Single
import me.tylerbwong.stack.data.model.*
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
        private const val KEY_PARAM = "key"
        private const val SEARCH_PARAM = "q"

        // defaults
        private const val DEFAULT_SITE = "stackoverflow"
        private const val DEFAULT_FILTER = "!-N4vhDh8TGjM*h(2reCz3exHc6q)hWsdi"
        private const val DEFAULT_SORT = ACTIVITY
        private const val DEFAULT_ORDER = DESC
        private const val DEFAULT_PAGE_SIZE = 50
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_KEY = ")vdLbYccKv*tSRXeypGGeA(("

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
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Single<Response<Question>>

    @GET("questions")
    fun getQuestionsByTags(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(TAGGED_PARAM) tags: String,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Single<Response<Question>>

    @GET("questions/{id}")
    fun getQuestionDetails(
            @Path("id") questionId: Int,
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Single<Response<Question>>

    @GET("questions/{id}/answers")
    fun getQuestionAnswers(
            @Path("id") questionId: Int,
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Single<Response<Answer>>

    @GET("search/advanced")
    fun getQuestionsBySearchString(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(SORT_PARAM) @Sort sort: String = RELEVANCE,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY,
            @Query(SEARCH_PARAM) searchString: String
    ): Single<Response<Question>>
}
