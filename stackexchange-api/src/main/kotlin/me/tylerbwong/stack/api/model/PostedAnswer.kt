package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This model is used for posted answers since some fields are omitted in the response regardless
 * of the filter passed through.
 */
@Serializable
data class PostedAnswer(
    @SerialName("answer_id")
    val answerId: Int,
    val owner: User,
    @SerialName("down_vote_count")
    val downVoteCount: Int = 0,
    @SerialName("up_vote_count")
    val upVoteCount: Int = 0,
    @SerialName("is_accepted")
    val isAccepted: Boolean,
    val score: Int,
    @SerialName("creation_date")
    val creationDate: Long,
    @SerialName("last_edit_date")
    val lastEditDate: Long?,
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("comment_count")
    val commentCount: Int?
)
