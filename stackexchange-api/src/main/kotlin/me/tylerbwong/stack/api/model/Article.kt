package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "article_id")
    val id: Int,
    @Json(name = "article_type")
    val articleType: String,
    @Json(name = "body_markdown")
    val bodyMarkdown: String?,
    @Json(name = "comment_count")
    val commentCount: Int?,
    @Json(name = "comments")
    val comments: List<Comment>?,
    @Json(name = "creation_date")
    val creationDate: Long?,
    @Json(name = "last_activity_date")
    val lastActivityDate: Long?,
    @Json(name = "last_edit_date")
    val lastEditDate: Long?,
    @Json(name = "last_editor")
    val lastEditor: User?,
    @Json(name = "link")
    val link: String,
    @Json(name = "owner")
    val owner: User,
    @Json(name = "score")
    val score: Int,
    @Json(name = "tags")
    val tags: List<String>?,
    @Json(name = "title")
    val title: String,
    @Json(name = "view_count")
    val viewCount: Int,
)
