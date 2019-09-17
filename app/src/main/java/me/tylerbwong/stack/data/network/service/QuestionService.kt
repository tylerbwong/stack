package me.tylerbwong.stack.data.network.service

import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.ORDER_PARAM
import me.tylerbwong.stack.data.model.Order
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.RELEVANCE
import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.model.SORT_PARAM
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestionService {

    @GET("questions")
    suspend fun getQuestions(
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
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
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Question>

    @GET("questions/{id}")
    suspend fun getQuestionDetails(
        @Path("id") questionId: Int,
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Question>

    @GET("questions/{id}")
    suspend fun getQuestionDetailsAuth(
        @Path("id") questionId: Int,
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Question>

    @GET("questions/{id}/linked")
    suspend fun getLinkedQuestions(
        @Path("id") questionId: Int,
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Question>

    @GET("questions/{id}/related")
    suspend fun getRelatedQuestions(
        @Path("id") questionId: Int,
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Question>

    @GET("questions/{id}/answers")
    suspend fun getQuestionAnswers(
        @Path("id") questionId: Int,
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Answer>

    @FormUrlEncoded
    @POST("questions/{id}/answers/add")
    suspend fun postAnswer(
        @Path("id") questionId: Int,
        @Field(SITE_PARAM) site: String = DEFAULT_SITE,
        @Field(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Field(BODY_PARAM) bodyMarkdown: String,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @GET("search/advanced")
    suspend fun getQuestionsBySearchString(
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(SORT_PARAM) @Sort sort: String = RELEVANCE,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Query(SEARCH_PARAM) searchString: String
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/downvote")
    suspend fun downvoteQuestionById(
        @Path("id") questionId: Int,
        @Field(SITE_PARAM) site: String = DEFAULT_SITE,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false,
        @Field("access_token") accessToken: String = AuthStore.accessToken ?: ""
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/downvote/undo")
    suspend fun undoQuestionDownvoteById(
        @Path("id") questionId: Int,
        @Field(SITE_PARAM) site: String = DEFAULT_SITE,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false,
        @Field("access_token") accessToken: String = AuthStore.accessToken ?: ""
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/favorite")
    suspend fun favoriteQuestionById(
        @Path("id") questionId: Int,
        @Field(SITE_PARAM) site: String = DEFAULT_SITE,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false,
        @Field("access_token") accessToken: String = AuthStore.accessToken ?: ""
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/favorite/undo")
    suspend fun undoQuestionFavoriteById(
        @Path("id") questionId: Int,
        @Field(SITE_PARAM) site: String = DEFAULT_SITE,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false,
        @Field("access_token") accessToken: String = AuthStore.accessToken ?: ""
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/upvote")
    suspend fun upvoteQuestionById(
        @Path("id") questionId: Int,
        @Field(SITE_PARAM) site: String = DEFAULT_SITE,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false,
        @Field("access_token") accessToken: String = AuthStore.accessToken ?: ""
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/upvote/undo")
    suspend fun undoQuestionUpvoteById(
        @Path("id") questionId: Int,
        @Field(SITE_PARAM) site: String = DEFAULT_SITE,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false,
        @Field("access_token") accessToken: String = AuthStore.accessToken ?: ""
    ): Response<Question>

    companion object {
        private const val BODY_PARAM = "body"
        private const val PREVIEW_PARAM = "preview"

        internal const val DEFAULT_FILTER = "!-N4vhDh8TGjM*h(2reCz3exHc6q)hWsdi"
        internal const val DETAIL_FILTER =
            "!3r.zRmD4l6rHdTgXfBOo(qq6rg_D3I7uaTO)p123.RRrNwbbeBOKxJp8dch552I"
        internal const val DETAIL_FILTER_AUTH =
            "!-(5KXGCBWX*gwD0lF1ueU_AM.fuZ5ajPdCrHQ*dmgQbj)i3*BIgm2.4v22"
    }
}
