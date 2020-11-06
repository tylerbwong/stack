package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.model.Badge
import me.tylerbwong.stack.api.model.NetworkUser
import me.tylerbwong.stack.api.model.ORDER_PARAM
import me.tylerbwong.stack.api.model.Order
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.SORT_PARAM
import me.tylerbwong.stack.api.model.TimelineEvent
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

    @GET("users/{userId}/timeline")
    suspend fun getUserTimeline(
        @Path(USER_ID) userId: Int?,
        @Query(FROM_DATE) fromDate: Long? = null,
        @Query(TO_DATE) toDate: Long? = null,
        @Query(FILTER_PARAM) filter: String = TIMELINE_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<TimelineEvent>

    @GET("users/{userId}/badges")
    suspend fun getUserBadges(
        @Path(USER_ID) userId: Int,
        @Query(FROM_DATE) fromDate: Long? = null,
        @Query(TO_DATE) toDate: Long? = null,
        @Query(SORT_PARAM) sort: String = BADGE_SORT,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = MAX_PAGE_SIZE,
        @Query(FILTER_PARAM) filter: String = BADGE_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<Badge>

    @GET("me/associated")
    suspend fun getCurrentUserNetworkUsers(
        @Query(FILTER_PARAM) filter: String = NETWORK_USER_FILTER,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    ): Response<NetworkUser>

    companion object {
        private const val USER_ID = "userId"
        private const val USER_FILTER = "!BTeL*Mb3d_KiD.hc7r8myHkxGjY*UT"

        private const val FROM_DATE = "fromdate"
        private const val TO_DATE = "todate"
        private const val TIMELINE_FILTER = "!9_R5-D7KQ"

        private const val BADGE_SORT = "rank"
        private const val BADGE_FILTER = "!6JEV(YmXqRgSv"

        private const val NETWORK_USER_FILTER = "!6QN8I_HG8vPBU"
    }
}
