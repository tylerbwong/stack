package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClosedDetails(
    @SerialName("description")
    val description: String,
    @SerialName("original_questions")
    val originalQuestions: List<OriginalQuestion> = emptyList(),
    @SerialName("reason")
    val reason: String,
) {
    val closedReason: ClosedReason
        get() = ClosedReason.values()
            .firstOrNull { reason.contains(it.reason, ignoreCase = true) }
            ?: ClosedReason.UNKNOWN

    val hasReason: Boolean
        get() = closedReason != ClosedReason.UNKNOWN

    enum class ClosedReason(val reason: String) {
        DUPLICATE("duplicate"),
        NOT_SUITABLE("not suitable for this site"),
        NEEDS_DETAILS("needs details"),
        NEEDS_FOCUS("needs more focus"),
        OPINION_BASED("opinion-based"),
        UNKNOWN("unknown");
    }
}

@Serializable
data class OriginalQuestion(
    @SerialName("accepted_answer_id")
    val acceptedAnswerId: Int? = null,
    @SerialName("answer_count")
    val answerCount: Int,
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("title")
    val title: String,
)
