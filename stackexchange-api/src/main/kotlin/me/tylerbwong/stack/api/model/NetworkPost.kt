package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPost(
    @Json(name = "post_id")
    val postId: Int,
    @Json(name = "post_type")
    val postType: String,
    @Json(name = "score")
    val score: Int,
    @Json(name = "title")
    val title: String
)
