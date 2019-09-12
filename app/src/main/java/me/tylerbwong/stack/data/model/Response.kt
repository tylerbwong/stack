package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName

data class Response<out T>(
    val items: List<T>,
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("error_id")
    val errorId: Int,
    @SerializedName("error_message")
    val errorMessage: String
)
