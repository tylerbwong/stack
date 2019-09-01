package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName
import me.tylerbwong.stack.data.auth.AuthStore

data class AnswerRequest(
        @SerializedName("body") val bodyMarkdown: String,
        @SerializedName("preview") private val preview: Boolean = false,
        @SerializedName("access_token") private val accessToken: String = AuthStore.accessToken ?: ""
) {
    companion object {
        fun from(bodyMarkdown: String) = AnswerRequest(bodyMarkdown = bodyMarkdown)
    }
}
