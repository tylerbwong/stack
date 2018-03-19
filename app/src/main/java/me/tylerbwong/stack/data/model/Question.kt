package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName

/**
 * Simplified model of a question.
 * TODO add owner data
 */
class Question(
        val tags: Array<String>,
        @SerializedName("is_answered")
        val isAnswered: Boolean,
        @SerializedName("view_count")
        val viewCount: Int,
        @SerializedName("answer_count")
        val answerCount: Int,
        val score: Int,
        @SerializedName("last_activity_date")
        val lastActivityDate: Long,
        @SerializedName("creation_date")
        val creationDate: Long,
        @SerializedName("question_id")
        val questionId: Int,
        val link: String,
        val title: String
)