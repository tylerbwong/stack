package me.tylerbwong.stack.ui.search

import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.data.model.SearchPayload

data class EmptySearchData(
    @Sort val sort: String,
    val tags: List<List<Tag>>,
    val searchHistory: List<SearchPayload>
)
