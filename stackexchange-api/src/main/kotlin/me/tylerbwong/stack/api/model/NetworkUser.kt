package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    @SerialName("account_id")
    val accountId: Int?,
    @SerialName("reputation")
    val reputation: Int = 0,
    @SerialName("site_name")
    val siteName: String,
    @SerialName("site_url")
    val siteUrl: String,
    @SerialName("user_id")
    val userId: Int = 0,
    @SerialName("user_type")
    val userType: String,
    @SerialName("badge_counts")
    val badgeCounts: BadgeCounts?,
    @SerialName("creation_date")
    val creationDate: Long,
    @SerialName("last_access_date")
    val lastAccessDate: Long,
    @SerialName("question_count")
    val questionCount: Int,
    @SerialName("top_answers")
    val topAnswers: List<NetworkPost>?,
    @SerialName("top_questions")
    val topQuestions: List<NetworkPost>?,
)
