package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    @SerialName("body_markdown")
    val bodyMarkdown: String = "",
    @SerialName("comment_id")
    val commentId: Int? = null,
    @SerialName("post_id")
    val postId: Int? = null,
    @SerialName("creation_date")
    val creationDate: Long,
    val edited: Boolean,
    @SerialName("owner")
    val owner: User,
    val score: Int? = null,
    val upvoted: Boolean? = null,
)
