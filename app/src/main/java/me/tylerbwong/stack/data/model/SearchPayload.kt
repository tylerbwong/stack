package me.tylerbwong.stack.data.model

sealed class SearchPayload {
    data class Advanced(
        val query: String,
        val isAccepted: Boolean? = null,
        val minNumAnswers: Int? = null,
        val bodyContains: String? = null,
        val isClosed: Boolean? = null,
        val tags: String? = null,
        val titleContains: String? = null
    ) : SearchPayload()
    data class Basic(val query: String) : SearchPayload()
    object Empty : SearchPayload()
}
