package me.tylerbwong.stack.api.utils

import kotlinx.serialization.json.Json
import me.tylerbwong.stack.api.model.ErrorResponse
import retrofit2.HttpException

const val ERROR_ID_INVALID_ACCESS_TOKEN = 402

fun HttpException.toErrorResponse(): ErrorResponse? {
    val json = response()?.errorBody()?.string() ?: return null
    return Json.decodeFromString(ErrorResponse.serializer(), json)
}
