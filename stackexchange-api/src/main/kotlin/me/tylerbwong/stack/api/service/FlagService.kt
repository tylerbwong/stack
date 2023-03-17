package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.Comment
import me.tylerbwong.stack.api.model.FlagOption
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FlagService {

    @FormUrlEncoded
    @POST("questions/{id}/flags/add")
    suspend fun addQuestionFlag(
        @Path("id") id: Int,
        @Field(OPTION_PARAM) optionId: Int,
        @Field(COMMENT_PARAM) comment: String? = null,
        @Field(TARGET_SITE_PARAM) targetSite: String? = null,
        @Field(QUESTION_ID_PARAM) questionId: Int? = null,
        @Field(FILTER_PARAM) filter: String = FLAG_FILTER,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Question>

    @GET("questions/{id}/flags/options")
    suspend fun getQuestionFlagOptions(
        @Path("id") id: Int,
        @Query(FILTER_PARAM) filter: String = FLAG_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<FlagOption>

    @FormUrlEncoded
    @POST("answers/{id}/flags/add")
    suspend fun addAnswerFlag(
        @Path("id") id: Int,
        @Field(OPTION_PARAM) optionId: Int,
        @Field(COMMENT_PARAM) comment: String? = null,
        @Field(TARGET_SITE_PARAM) targetSite: String? = null,
        @Field(QUESTION_ID_PARAM) questionId: Int? = null,
        @Field(FILTER_PARAM) filter: String = FLAG_FILTER,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Answer>

    @GET("answers/{id}/flags/options")
    suspend fun getAnswerFlagOptions(
        @Path("id") id: Int,
        @Query(FILTER_PARAM) filter: String = FLAG_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<FlagOption>

    @FormUrlEncoded
    @POST("comments/{id}/flags/add")
    suspend fun addCommentFlag(
        @Path("id") id: Int,
        @Field(OPTION_PARAM) optionId: Int,
        @Field(COMMENT_PARAM) comment: String? = null,
        @Field(TARGET_SITE_PARAM) targetSite: String? = null,
        @Field(QUESTION_ID_PARAM) questionId: Int? = null,
        @Field(FILTER_PARAM) filter: String = FLAG_FILTER,
        @Field(KEY_PARAM) key: String = BuildConfig.API_KEY,
        @Field(PREVIEW_PARAM) preview: Boolean = false
    ): Response<Comment>

    @GET("comments/{id}/flags/options")
    suspend fun getCommentFlagOptions(
        @Path("id") id: Int,
        @Query(FILTER_PARAM) filter: String = FLAG_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<FlagOption>

    companion object {
        private const val OPTION_PARAM = "option_id"
        private const val COMMENT_PARAM = "comment"
        private const val TARGET_SITE_PARAM = "target_site"
        private const val QUESTION_ID_PARAM = "question_id"
        private const val FLAG_FILTER = "!nOedRLgr.z"
    }
}
