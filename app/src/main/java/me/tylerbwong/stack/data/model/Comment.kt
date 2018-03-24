package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("body_markdown")
    val bodyMarkdown: String,
    @SerializedName("comment_id")
    val commentId: Int,
    @SerializedName("creation_date")
    val creationDate: Long,
    val edited: Boolean,
    val owner: User,
    val score: Int
)