package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface AnswerService {

    @FormUrlEncoded
    @POST("answers/{id}/downvote")
    suspend fun downvoteAnswerById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(QuestionService.PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @FormUrlEncoded
    @POST("answers/{id}/downvote/undo")
    suspend fun undoAnswerDownvoteById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(QuestionService.PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @FormUrlEncoded
    @POST("answers/{id}/upvote")
    suspend fun upvoteAnswerById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(QuestionService.PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @FormUrlEncoded
    @POST("answers/{id}/upvote/undo")
    suspend fun undoAnswerUpvoteById(
        @Path("id") answerId: Int,
        @Field(FILTER_PARAM) filter: String = ANSWER_FILTER_AUTH,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(QuestionService.PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    companion object {
        internal const val ANSWER_FILTER =
            "!HzgO6Jg6sME4H_1vS70d6DFK5mYr)2q1N44cbmn5TW)L6gY9nV-Fj8ZYP1cBg6yrUwoXjNbrH5Z**AepUh.i0op2_hMhQ"
        internal const val ANSWER_FILTER_AUTH =
            "!)aHQ9FGlxVZ-FDR2obogNxnqETJo9DXW96Zvv3FLjN(pPDs04v10AoUeW*Sb9Wk7MEfS51yyUe8irkMqfVxdA9yBv3-"
    }
}
