package me.tylerbwong.stack.data.model

import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.utils.formatElapsedTime

data class QuestionDraft(
    val id: Int,
    val title: String,
    private val updatedDate: Long,
    val body: String,
    val tags: String,
    val site: String
) {
    val formattedTimestamp = updatedDate.formatElapsedTime(ApplicationWrapper.context)
}
