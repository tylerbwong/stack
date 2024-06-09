package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("error_id")
    val errorId: Int = -1,
    @SerialName("error_message")
    val errorMessage: String = "",
    @SerialName("error_name")
    val errorName: String = ""
)
