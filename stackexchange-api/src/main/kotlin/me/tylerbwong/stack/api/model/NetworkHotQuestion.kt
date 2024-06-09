package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHotQuestion(
    @SerialName("site")
    val site: String,
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("display_score")
    val displayScore: Double,
    @SerialName("icon_url")
    val iconUrl: String,
    @SerialName("creation_date")
    val creationDate: Long,
    @SerialName("answer_count")
    val answerCount: Int,
    @SerialName("user_name")
    val userName: String,
    @SerialName("tags")
    val tags: List<String>
)
