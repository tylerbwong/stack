package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUser(
    @Json(name = "account_id")
    val accountId: Int?,
    @Json(name = "reputation")
    val reputation: Int = 0,
    @Json(name = "site_name")
    val siteName: String,
    @Json(name = "site_url")
    val siteUrl: String,
    @Json(name = "user_id")
    val userId: Int = 0,
    @Json(name = "user_type")
    val userType: String,
    @Json(name = "badge_counts")
    val badgeCounts: BadgeCounts?,
    @Json(name = "creation_date")
    val creationDate: Long,
    @Json(name = "last_access_date")
    val lastAccessDate: Long,
    @Json(name = "question_count")
    val questionCount: Int,
    @Json(name = "top_answers")
    val topAnswers: List<NetworkPost>,
    @Json(name = "top_questions")
    val topQuestions: List<NetworkPost>
)
