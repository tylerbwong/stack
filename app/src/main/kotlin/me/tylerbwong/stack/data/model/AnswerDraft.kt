package me.tylerbwong.stack.data.model

data class AnswerDraft(
    val questionId: Int,
    val questionTitle: String,
    val formattedTimestamp: String,
    val bodyMarkdown: String,
    val site: String
)
