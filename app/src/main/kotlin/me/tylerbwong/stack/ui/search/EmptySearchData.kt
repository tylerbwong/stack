package me.tylerbwong.stack.ui.search

import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.data.model.SearchPayload

data class EmptySearchData(
    val tags: List<Tag>,
    val searchHistory: List<SearchPayload>
)
