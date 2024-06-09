package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlagOption(
    @SerialName("count")
    val count: Int?,
    @SerialName("description")
    val description: String?,
    @SerialName("dialog_title")
    val dialogTitle: String?,
    @SerialName("has_flagged")
    val hasFlagged: Boolean?,
    @SerialName("is_retraction")
    val isRetraction: Boolean?,
    @SerialName("option_id")
    val optionId: Int?,
    @SerialName("requires_comment")
    val requiresComment: Boolean?,
    @SerialName("requires_question_id")
    val requiresQuestionId: Boolean?,
    @SerialName("requires_site")
    val requiresSite: Boolean?,
    @SerialName("sub_options")
    val subOptions: List<FlagOption>?,
    @SerialName("title")
    val title: String?,
)
