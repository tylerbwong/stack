package me.tylerbwong.stack.data.model

sealed class SearchPayload {

    data class Standard(
        val query: String,
        val isAccepted: Boolean? = null,
        val minNumAnswers: Int? = null,
        val bodyContains: String? = null,
        val isClosed: Boolean? = null,
        val tags: List<String>? = null,
        val titleContains: String? = null,
        val searchId: Int? = null
    ) : SearchPayload()

    object Empty : SearchPayload()
}
