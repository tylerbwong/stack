package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Question model.
 */
@JsonClass(generateAdapter = true)
data class Question(
    @Json(name = "answer_count")
    val answerCount: Int = 0,
    val body: String?,
    @Json(name = "body_markdown")
    val bodyMarkdown: String?,
    @Json(name = "closed_date")
    val closedDate: Long?,
    @Json(name = "closed_reason")
    val closedReason: String?,
    @Json(name = "comment_count")
    val commentCount: Int?,
    @Json(name = "creation_date")
    val creationDate: Long,
    @Json(name = "down_vote_count")
    val downVoteCount: Int = 0,
    @Json(name = "downvoted")
    val isDownVoted: Boolean = false,
    @Json(name = "favorited")
    val isBookmarked: Boolean = false,
    @Json(name = "favorite_count")
    val bookmarkCount: Int = 0,
    @Json(name = "is_answered")
    val isAnswered: Boolean,
    @Json(name = "last_activity_date")
    val lastActivityDate: Long?,
    @Json(name = "last_edit_date")
    val lastEditDate: Long?,
    @Json(name = "last_editor")
    val lastEditor: User?,
    val owner: User,
    @Json(name = "question_id")
    val questionId: Int = -1,
    val score: Int,
    @Json(name = "share_link")
    val shareLink: String = "",
    val tags: List<String>?,
    val title: String,
    @Json(name = "up_vote_count")
    val upVoteCount: Int = 0,
    @Json(name = "upvoted")
    val isUpVoted: Boolean = false,
    @Json(name = "view_count")
    val viewCount: Int = 0
)
