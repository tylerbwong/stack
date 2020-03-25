package me.tylerbwong.stack.data.model

sealed class SearchPayload {

    data class Standard(
        val query: String,
        val isAccepted: Boolean? = false,
        val minNumAnswers: Int? = 4,
        val bodyContains: String? = "edittext",
        val isClosed: Boolean? = false,
        val tags: List<String>? = listOf("android"),
        val titleContains: String? = null,
        val searchId: Int? = null
    ) : SearchPayload()

    object Empty : SearchPayload()
}
