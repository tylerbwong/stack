package me.tylerbwong.stack.ui.search

import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.model.Tag

data class EmptySearchData(
    val tags: List<Tag>,
    val searchHistory: List<SearchPayload>
)
