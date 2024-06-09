package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response<out T>(
    @SerialName("items")
    val items: List<T> = emptyList(),
    @SerialName("has_more")
    val hasMore: Boolean = false
) {
    companion object {
        val EMPTY = Response<Unit>()
    }
}
