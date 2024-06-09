package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Question model.
 */
@Serializable
data class Question(
    @SerialName("answer_count")
    val answerCount: Int = 0,
    val body: String?,
    @SerialName("body_markdown")
    val bodyMarkdown: String?,
    @SerialName("closed_date")
    val closedDate: Long?,
    @SerialName("closed_reason")
    val closedReason: String?,
    @SerialName("closed_details")
    val closedDetails: ClosedDetails? = null,
    @SerialName("comment_count")
    val commentCount: Int?,
    @SerialName("creation_date")
    val creationDate: Long,
    @SerialName("down_vote_count")
    val downVoteCount: Int = 0,
    @SerialName("downvoted")
    val isDownVoted: Boolean = false,
    @SerialName("favorited")
    val isBookmarked: Boolean = false,
    @SerialName("favorite_count")
    val bookmarkCount: Int = 0,
    @SerialName("is_answered")
    val isAnswered: Boolean,
    @SerialName("last_activity_date")
    val lastActivityDate: Long?,
    @SerialName("last_edit_date")
    val lastEditDate: Long?,
    @SerialName("last_editor")
    val lastEditor: User?,
    val owner: User,
    @SerialName("question_id")
    val questionId: Int = -1,
    val score: Int,
    @SerialName("share_link")
    val shareLink: String = "",
    val tags: List<String>?,
    val title: String,
    @SerialName("up_vote_count")
    val upVoteCount: Int = 0,
    @SerialName("upvoted")
    val isUpVoted: Boolean = false,
    @SerialName("view_count")
    val viewCount: Int = 0
)
