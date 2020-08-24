package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "body_markdown")
    val bodyMarkdown: String = "",
    @Json(name = "comment_id")
    val commentId: Int,
    @Json(name = "creation_date")
    val creationDate: Long,
    val edited: Boolean,
    @Json(name = "owner")
    val owner: User,
    val score: Int
)
