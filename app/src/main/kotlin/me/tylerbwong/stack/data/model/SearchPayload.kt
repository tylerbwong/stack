package me.tylerbwong.stack.data.model

data class SearchPayload(
    val query: String,
    val isAccepted: Boolean? = null,
    val minNumAnswers: Int? = null,
    val bodyContains: String? = null,
    val isClosed: Boolean? = null,
    val tags: List<String>? = null,
    val titleContains: String? = null
) {
    fun isNotEmpty() = query.isNotEmpty() || listOf(
        isAccepted,
        minNumAnswers,
        bodyContains,
        isClosed,
        tags,
        titleContains
    ).any { it != null }

    companion object {
        fun empty() = SearchPayload("")
    }
}
