package me.tylerbwong.stack.data.model

data class QuestionDraft(
    val id: Int,
    val title: String,
    val formattedTimestamp: String,
    val body: String,
    val tags: String,
    val site: String
)
