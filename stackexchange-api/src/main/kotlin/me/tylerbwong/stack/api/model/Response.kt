package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response<out T>(
    @Json(name = "items")
    val items: List<T> = emptyList(),
    @Json(name = "has_more")
    val hasMore: Boolean = false
) {
    companion object {
        val EMPTY = Response<Unit>()
    }
}
