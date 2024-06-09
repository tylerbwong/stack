package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InboxItem(
    @SerialName("answer_id")
    val answerId: Int?,
    @SerialName("body")
    val body: String?,
    @SerialName("comment_id")
    val commentId: Int?,
    @SerialName("creation_date")
    val creationDate: Long,
    @SerialName("is_unread")
    val isUnread: Boolean,
    @SerialName("item_type")
    val itemType: String,
    @SerialName("link")
    val link: String,
    @SerialName("question_id")
    var questionId: Int?,
    @SerialName("site")
    val site: Site?,
    @SerialName("title")
    val title: String?,
)
