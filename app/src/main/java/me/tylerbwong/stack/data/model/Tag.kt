package me.tylerbwong.stack.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "name")
    val name: String,
    @Json(name = "last_activity_date")
    val lastActivityDate: Long? = 0L,
    @Json(name = "count")
    val count: Int
)
