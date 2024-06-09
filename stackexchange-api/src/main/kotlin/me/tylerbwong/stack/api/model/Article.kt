package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    @SerialName("article_id")
    val id: Int,
    @SerialName("article_type")
    val articleType: String,
    @SerialName("body_markdown")
    val bodyMarkdown: String?,
    @SerialName("comment_count")
    val commentCount: Int?,
    @SerialName("comments")
    val comments: List<Comment>?,
    @SerialName("creation_date")
    val creationDate: Long?,
    @SerialName("last_activity_date")
    val lastActivityDate: Long?,
    @SerialName("last_edit_date")
    val lastEditDate: Long?,
    @SerialName("last_editor")
    val lastEditor: User?,
    @SerialName("link")
    val link: String,
    @SerialName("owner")
    val owner: User,
    @SerialName("score")
    val score: Int,
    @SerialName("tags")
    val tags: List<String>?,
    @SerialName("title")
    val title: String,
    @SerialName("view_count")
    val viewCount: Int,
)
