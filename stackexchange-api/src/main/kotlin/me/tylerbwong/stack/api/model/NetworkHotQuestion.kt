package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkHotQuestion(
    @Json(name = "site")
    val site: String,
    @Json(name = "question_id")
    val questionId: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "display_score")
    val displayScore: Double,
    @Json(name = "icon_url")
    val iconUrl: String,
    @Json(name = "creation_date")
    val creationDate: Long,
    @Json(name = "answer_count")
    val answerCount: Int,
    @Json(name = "user_name")
    val userName: String,
    @Json(name = "tags")
    val tags: List<String>
)
