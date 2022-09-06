package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagPreference(
    @Json(name = "tag_name")
    val tagName: String,
    @Json(name = "tag_preference_type")
    val tagPreferenceType: String,
)
