package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("me")
    suspend fun getCurrentUser(
        @Query(FILTER_PARAM) filter: String = USER_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<User>

    @GET("users/{userId}")
    suspend fun getUser(
        @Path(USER_ID) userId: Int?,
        @Query(FILTER_PARAM) filter: String = USER_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<User>

    @GET("users/{userId}/questions")
    suspend fun getUserQuestionsById(
        @Path(USER_ID) userId: Int?,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = QuestionService.DEFAULT_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Question>

    @GET("users/{userId}/answers")
    suspend fun getUserAnswersById(
        @Path(USER_ID) userId: Int?,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = QuestionService.DETAIL_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Answer>

    companion object {
        private const val USER_ID = "userId"
        private const val USER_FILTER = "!BTeL*Mb3d_KiD.hc7r8myHkxGjY*UT"
    }
}
