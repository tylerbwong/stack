package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "error_id")
    val errorId: Int = -1,
    @Json(name = "error_message")
    val errorMessage: String = "",
    @Json(name = "error_name")
    val errorName: String = ""
) {
    companion object {
        const val WRITE_FAILED = "write_failed"
    }
}
