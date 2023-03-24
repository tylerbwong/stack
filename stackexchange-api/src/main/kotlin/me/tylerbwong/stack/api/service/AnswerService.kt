package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.ORDER_PARAM
import me.tylerbwong.stack.api.model.Order
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

interface AnswerService {

    @GET("answers/{id}")
    suspend fun getAnswerById(
        @Path("id") answerId: Int,
        @Query(SORT_PARAM) @Sort sort: String = VOTES,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = ANSWER_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Answer>

    @GET("answers/{id}")
    suspend fun getAnswerByIdAuth(
        @Path("id") answerId: Int,
        @Query(SORT_PARAM) @Sort sort: String = VOTES,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Answer>

    @FormUrlEncoded
    @POST("answers/{id}/downvote")
    suspend fun downvoteAnswerById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @FormUrlEncoded
    @POST("answers/{id}/downvote/undo")
    suspend fun undoAnswerDownvoteById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @FormUrlEncoded
    @POST("answers/{id}/upvote")
    suspend fun upvoteAnswerById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @FormUrlEncoded
    @POST("answers/{id}/upvote/undo")
    suspend fun undoAnswerUpvoteById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    companion object {
        internal const val ANSWER_FILTER =
            "!HzgO6Jg6sME4H_1vS70d6DFK5mYr)2q1N44cbmn5TW)L6gY9nV-Fj8ZYP1cBg6yrUwoXjNbrH5Z**AZ0K6WAi(e*BgAoz"
        internal const val ANSWER_FILTER_AUTH =
            "!)aHQ9FGlxVZ-FDR2obogNxnqETJo9DXW96Zvv3FLjN(pPDs04v10AoUeW*Sb9Wk7MEfS51yyUe8irkMLAlVu0.uRXjY"
    }
}
