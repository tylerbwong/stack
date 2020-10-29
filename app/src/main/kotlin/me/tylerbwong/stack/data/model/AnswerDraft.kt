package me.tylerbwong.stack.data.model

import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.utils.formatElapsedTime

data class AnswerDraft(
    val questionId: Int,
    val questionTitle: String,
    private val updatedDate: Long,
    val bodyMarkdown: String,
    val site: String
) {
    val formattedTimestamp = updatedDate.formatElapsedTime(ApplicationWrapper.context)
}
