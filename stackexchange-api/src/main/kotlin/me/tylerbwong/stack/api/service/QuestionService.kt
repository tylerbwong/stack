package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.ORDER_PARAM
import me.tylerbwong.stack.api.model.Order
import me.tylerbwong.stack.api.model.PostedAnswer
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.SORT_PARAM
import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.api.model.VOTES
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestionService {

    @GET("questions")
    suspend fun getQuestions(
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("me/favorites")
    suspend fun getBookmarks(
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("questions")
    suspend fun getQuestionsByTags(
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(TAGGED_PARAM) tags: String,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("questions/{id}")
    suspend fun getQuestionDetails(
        @Path("id") questionId: Int,
        @Query(FILTER_PARAM) filter: String = DETAIL_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("questions/{id}")
    suspend fun getQuestionDetailsAuth(
        @Path("id") questionId: Int,
        @Query(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("questions/{id}/linked")
    suspend fun getLinkedQuestions(
        @Path("id") questionId: Int,
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("questions/{id}/related")
    suspend fun getRelatedQuestions(
        @Path("id") questionId: Int,
        @Query(SORT_PARAM) @Sort sort: String = DEFAULT_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("questions/{id}/answers")
    suspend fun getQuestionAnswers(
        @Path("id") questionId: Int,
        @Query(SORT_PARAM) @Sort sort: String = VOTES,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = ANSWER_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Answer>

    @FormUrlEncoded
    @POST("questions/{id}/answers/add")
    suspend fun postAnswer(
        @Path("id") questionId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(BODY_PARAM) bodyMarkdown: String,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<PostedAnswer>

    @FormUrlEncoded
    @POST("questions/add")
    suspend fun addQuestion(
        @Field(TITLE_PARAM) title: String,
        @Field(BODY_PARAM) body: String,
        @Field(TAGS_PARAM) tags: String,
        @Field(FILTER_PARAM) filter: String = DEFAULT_FILTER,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/downvote")
    suspend fun downvoteQuestionById(
        @Path("id") questionId: Int,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/downvote/undo")
    suspend fun undoQuestionDownvoteById(
        @Path("id") questionId: Int,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/favorite")
    suspend fun favoriteQuestionById(
        @Path("id") questionId: Int,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/favorite/undo")
    suspend fun undoQuestionFavoriteById(
        @Path("id") questionId: Int,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/upvote")
    suspend fun upvoteQuestionById(
        @Path("id") questionId: Int,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    @FormUrlEncoded
    @POST("questions/{id}/upvote/undo")
    suspend fun undoQuestionUpvoteById(
        @Path("id") questionId: Int,
        @Field(FILTER_PARAM) filter: String = DETAIL_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    companion object {
        private const val TITLE_PARAM = "title"
        private const val BODY_PARAM = "body"
        private const val TAGS_PARAM = "tags"
        private const val PREVIEW_PARAM = "preview"

        internal const val DEFAULT_FILTER = "!BKmYcFXnoJ*)bD9xee.1*pffbdPT9("
        internal const val ANSWER_FILTER =
            "!)qHt3-n4W*f.gPKawPE(XNpsQRxcWdBxS2t8_drqo0.DgldgGwePa9Pib9nUlpc.1r"
        internal const val DETAIL_FILTER =
            "!3r.zRmD4l6rHdTgXfBOo(qq6rg_D3I7uaTO)p123.RRrNwbbeBOKxJp8dch552I"
        internal const val DETAIL_FILTER_AUTH =
            "!-(5KXGCBWX*gwD0lF1ueU_AM.fuZ5ajPdCrHQ*dmgQbj)i3*BIgm2.4v22"
    }
}
