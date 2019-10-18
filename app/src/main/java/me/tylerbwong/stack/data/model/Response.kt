package me.tylerbwong.stack.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response<out T>(
    val items: List<T>,
    @Json(name = "has_more")
    val hasMore: Boolean
)
