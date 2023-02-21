package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClosedDetails(
    @Json(name = "description")
    val description: String,
    @Json(name = "original_questions")
    val originalQuestions: List<OriginalQuestion> = emptyList(),
    @Json(name = "reason")
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
        OPINION_BASED("opinion based"),
        UNKNOWN("unknown");
    }
}

@JsonClass(generateAdapter = true)
data class OriginalQuestion(
    @Json(name = "accepted_answer_id")
    val acceptedAnswerId: Int? = null,
    @Json(name = "answer_count")
    val answerCount: Int,
    @Json(name = "question_id")
    val questionId: Int,
    @Json(name = "title")
    val title: String,
)
