package me.tylerbwong.stack.data.model

/**
 * Model for the response returned from /questions
 */
data class QuestionResponse(
        val items: List<Question>,
        val has_more: Boolean
)