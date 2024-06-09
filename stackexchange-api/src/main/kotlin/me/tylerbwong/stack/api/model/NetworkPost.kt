package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPost(
    @SerialName("post_id")
    val postId: Int,
    @SerialName("post_type")
    val postType: String,
    @SerialName("score")
    val score: Int,
    @SerialName("title")
    val title: String
)
