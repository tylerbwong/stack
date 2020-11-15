package me.tylerbwong.stack.ui.search

import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Sort

data class SearchData(
    @Sort val sort: String,
    val questions: List<Question>
)
