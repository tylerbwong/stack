package me.tylerbwong.stack.ui.utils

import com.squareup.moshi.Moshi
import me.tylerbwong.stack.data.model.ErrorResponse
import retrofit2.HttpException

fun HttpException.toErrorResponse(): ErrorResponse? {
    val moshi = Moshi.Builder().build()
    val adapter = moshi.adapter(ErrorResponse::class.java)
    return response()?.errorBody()?.string()?.let { adapter.fromJson(it) }
}
