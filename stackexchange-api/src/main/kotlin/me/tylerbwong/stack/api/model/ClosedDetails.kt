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
    val isDuplicate: Boolean
        get() = reason.equals("duplicate", ignoreCase = true)
}

@JsonClass(generateAdapter = true)
data class OriginalQuestion(
    @Json(name = "accepted_answer_id")
    val acceptedAnswerId: Int,
    @Json(name = "answer_count")
    val answerCount: Int,
    @Json(name = "question_id")
    val questionId: Int,
    @Json(name = "title")
    val title: String,
)
