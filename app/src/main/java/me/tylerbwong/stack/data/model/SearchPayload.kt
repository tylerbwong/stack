package me.tylerbwong.stack.data.model

sealed class SearchPayload {

    @Suppress("UNCHECKED_CAST")
    open operator fun <T : SearchPayload> plus(other: SearchPayload): T = Empty as T

    data class Advanced(
        val query: String,
        val isAccepted: Boolean? = null,
        val minNumAnswers: Int? = null,
        val bodyContains: String? = null,
        val isClosed: Boolean? = null,
        val tags: List<String>? = null,
        val titleContains: String? = null
    ) : SearchPayload() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : SearchPayload> plus(other: SearchPayload): T {
            return when (other) {
                is Advanced, is Empty -> other
                is Basic -> copy(query = other.query)
            } as T
        }
    }

    data class Basic(val query: String) : SearchPayload() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : SearchPayload> plus(other: SearchPayload): T {
            return when (other) {
                is Advanced, is Empty -> other
                is Basic -> copy(query = other.query)
            } as T
        }
    }

    object Empty : SearchPayload() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : SearchPayload> plus(other: SearchPayload): T {
            return when (other) {
                is Advanced, is Basic, is Empty -> other
            } as T
        }
    }
}
