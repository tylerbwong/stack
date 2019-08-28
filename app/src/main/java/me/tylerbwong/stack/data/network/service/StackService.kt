package me.tylerbwong.stack.data.network.service

import me.tylerbwong.stack.data.model.ACTIVITY
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.DESC
import me.tylerbwong.stack.data.model.ORDER_PARAM
import me.tylerbwong.stack.data.model.Order
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.RELEVANCE
import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.model.SORT_PARAM
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.network.ServiceProvider.DEFAULT_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackService {

    @GET("questions")
    suspend fun getQuestions(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<Question>

    @GET("questions")
    suspend fun getQuestionsByTags(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(TAGGED_PARAM) tags: String,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<Question>

    @GET("questions/{id}")
    suspend fun getQuestionDetails(
            @Path("id") questionId: Int,
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<Question>

    @GET("questions/{id}/answers")
    suspend fun getQuestionAnswers(
            @Path("id") questionId: Int,
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<Answer>

    @GET("search/advanced")
    suspend fun getQuestionsBySearchString(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(SORT_PARAM) @Sort sort: String = RELEVANCE,
            @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY,
            @Query(SEARCH_PARAM) searchString: String
    ): Response<Question>

    @GET("users/{userId}/questions")
    suspend fun getUserQuestionsById(
            @Path("userId") userId: Int?,
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<Question>

    @GET("users/{userId}/answers")
    suspend fun getUserAnswersById(
            @Path("userId") userId: Int?,
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
            @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
            @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<Answer>

    @GET("me")
    suspend fun getCurrentUser(
            @Query(SITE_PARAM) site: String = DEFAULT_SITE,
            @Query(FILTER_PARAM) filter: String = CURRENT_USER_FILTER,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<User>

    @GET("access-tokens/{accessToken}/invalidate")
    suspend fun logOut(
            @Path(ACCESS_TOKEN) accessToken: String,
            @Query(KEY_PARAM) key: String = DEFAULT_KEY
    ): Response<Unit>

    companion object {

        // query params
        private const val ACCESS_TOKEN = "accessToken"
        private const val SITE_PARAM = "site"
        private const val PAGE_SIZE_PARAM = "pagesize"
        private const val PAGE_PARAM = "page"
        private const val FILTER_PARAM = "filter"
        private const val TAGGED_PARAM = "tagged"
        private const val KEY_PARAM = "key"
        private const val SEARCH_PARAM = "q"

        // defaults
        private const val DEFAULT_SITE = "stackoverflow"
        private const val DEFAULT_FILTER = "!-N4vhDh8TGjM*h(2reCz3exHc6q)hWsdi"
        @Sort
        private const val DEFAULT_SORT = ACTIVITY
        @Order
        private const val DEFAULT_ORDER = DESC
        private const val DEFAULT_PAGE_SIZE = 50
        private const val DEFAULT_PAGE = 1

        // detail
        private const val DETAIL_FILTER = "!3r.zRmD4l6rHdTgXfBOo(qq6rg_D3I7uaTO)p123.RRrNwbbeBOKxJp8dch552I"

        // current user
        private const val CURRENT_USER_FILTER = "!BTeL*Mb3d_KiD.hc7r8myHkxGjY*UT"
    }
}
