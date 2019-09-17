package me.tylerbwong.stack.data.auth.utils

import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import timber.log.Timber

private const val FORM_URL_ENCODED = "application/x-www-form-urlencoded; charset=utf-8"

fun RequestBody.addField(name: String, value: String, isEncoded: Boolean = true): RequestBody {
    val formBody = FormBody.Builder().apply {
        if (isEncoded) {
            addEncoded(name, value)
        } else {
            add(name, value)
        }
    }.build()

    val requestBodyString = toBodyString()
    val separator = if (requestBodyString.isNotBlank()) "&" else ""
    val formBodyString = formBody.toBodyString()
    val combined = "$requestBodyString$separator$formBodyString"
    return combined.toRequestBody(FORM_URL_ENCODED.toMediaTypeOrNull())
}

private fun RequestBody.toBodyString(): String {
    return try {
        Buffer().use {
            writeTo(it)
            it.readUtf8()
        }
    } catch (ex: Exception) {
        Timber.e(ex)
        ""
    }
}
