package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("answer_id")
    val answerId: Int,
    val owner: User,
    @SerializedName("down_vote_count")
    val downVoteCount: Int,
    @SerializedName("up_vote_count")
    val upVoteCount: Int,
    @SerializedName("is_accepted")
    val isAccepted: Boolean,
    val score: Int,
    @SerializedName("creation_date")
    val creationDate: Long,
    @SerializedName("body_markdown")
    val bodyMarkdown: String,
    @SerializedName("last_editor")
    val lastEditor: User?,
    val comments: List<Comment>
)
