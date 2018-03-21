package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response returned from /questions
 */
data class QuestionResponse(
        val items: List<Question>,
        @SerializedName("has_more")
        val hasMore: Boolean
)