package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Answer(
    @SerialName("answer_id")
    val answerId: Int,
    val owner: User,
    @SerialName("down_vote_count")
    val downVoteCount: Int = 0,
    @SerialName("up_vote_count")
    val upVoteCount: Int = 0,
    @SerialName("is_accepted")
    val isAccepted: Boolean,
    @SerialName("upvoted")
    val isUpvoted: Boolean? = null,
    @SerialName("downvoted")
    val isDownvoted: Boolean? = null,
    val score: Int,
    @SerialName("creation_date")
    val creationDate: Long,
    @SerialName("body_markdown")
    val bodyMarkdown: String = "",
    @SerialName("last_edit_date")
    val lastEditDate: Long?,
    @SerialName("last_editor")
    val lastEditor: User?,
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("comment_count")
    val commentCount: Int?,
    @SerialName("share_link")
    val shareLink: String = "",
    @SerialName("title")
    val title: String = "",
)
