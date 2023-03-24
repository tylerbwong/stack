package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InboxItem(
    @Json(name = "answer_id")
    val answerId: Int?,
    @Json(name = "body")
    val body: String?,
    @Json(name = "comment_id")
    val commentId: Int?,
    @Json(name = "creation_date")
    val creationDate: Long,
    @Json(name = "is_unread")
    val isUnread: Boolean,
    @Json(name = "item_type")
    val itemType: String,
    @Json(name = "link")
    val link: String,
    @Json(name = "question_id")
    var questionId: Int?,
    @Json(name = "site")
    val site: Site?,
    @Json(name = "title")
    val title: String,
)
