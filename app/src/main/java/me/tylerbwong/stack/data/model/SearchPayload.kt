package me.tylerbwong.stack.data.model

data class SearchPayload(
    val query: String = "",
    val isAccepted: Boolean? = null,
    val minNumAnswers: Int? = null,
    val bodyContains: String? = null,
    val isClosed: Boolean? = null,
    val tags: String? = null,
    val titleContains: String? = null
)
