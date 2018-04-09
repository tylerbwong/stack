package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName

/**
 * Question model.
 */
data class Question(
        @SerializedName("answer_count")
        val answerCount: Int,
        val body: String?,
        @SerializedName("body_markdown")
        val bodyMarkdown: String?,
        @SerializedName("closed_date")
        val closedDate: Long?,
        @SerializedName("closed_reason")
        val closedReason: String?,
        @SerializedName("comment_count")
        val commentCount: Int?,
        @SerializedName("creation_date")
        val creationDate: Long,
        @SerializedName("down_vote_count")
        val downVoteCount: Int,
        @SerializedName("favorite_count")
        val favoriteCount: Int,
        @SerializedName("is_answered")
        val isAnswered: Boolean,
        @SerializedName("last_activity_date")
        val lastActivityDate: Long?,
        @SerializedName("last_edit_date")
        val lastEditDate: Long?,
        @SerializedName("last_editor")
        val lastEditor: User?,
        val owner: User,
        @SerializedName("question_id")
        val questionId: Int,
        val score: Int,
        @SerializedName("share_link")
        val shareLink: String,
        val tags: List<String>?,
        val title: String,
        @SerializedName("up_vote_count")
        val upVoteCount: Int,
        @SerializedName("view_count")
        val viewCount: Int
)
