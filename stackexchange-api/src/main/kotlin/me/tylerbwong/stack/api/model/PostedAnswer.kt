package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This model is used for posted answers since some fields are omitted in the response regardless
 * of the filter passed through.
 */
@JsonClass(generateAdapter = true)
data class PostedAnswer(
    @Json(name = "answer_id")
    val answerId: Int,
    val owner: User,
    @Json(name = "down_vote_count")
    val downVoteCount: Int = 0,
    @Json(name = "up_vote_count")
    val upVoteCount: Int = 0,
    @Json(name = "is_accepted")
    val isAccepted: Boolean,
    val score: Int,
    @Json(name = "creation_date")
    val creationDate: Long,
    @Json(name = "last_edit_date")
    val lastEditDate: Long?,
    @Json(name = "question_id")
    val questionId: Int,
    @Json(name = "comment_count")
    val commentCount: Int?
)
