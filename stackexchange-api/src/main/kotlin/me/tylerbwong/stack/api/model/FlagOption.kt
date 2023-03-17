package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlagOption(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "dialog_title")
    val dialogTitle: String?,
    @Json(name = "has_flagged")
    val hasFlagged: Boolean?,
    @Json(name = "is_retraction")
    val isRetraction: Boolean?,
    @Json(name = "option_id")
    val optionId: Int?,
    @Json(name = "requires_comment")
    val requiresComment: Boolean?,
    @Json(name = "requires_question_id")
    val requiresQuestionId: Boolean?,
    @Json(name = "requires_site")
    val requiresSite: Boolean?,
    @Json(name = "sub_options")
    val subOptions: List<FlagOption>?,
    @Json(name = "title")
    val title: String?,
)
