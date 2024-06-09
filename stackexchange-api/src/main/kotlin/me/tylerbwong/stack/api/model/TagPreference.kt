package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagPreference(
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("tag_preference_type")
    val tagPreferenceType: String,
)
