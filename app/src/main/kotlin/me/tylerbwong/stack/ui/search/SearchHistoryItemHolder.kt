package me.tylerbwong.stack.ui.search

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.SearchHistoryItemHolderBinding
import me.tylerbwong.stack.ui.home.SearchHistoryItem
import me.tylerbwong.stack.ui.search.filters.Filter
import me.tylerbwong.stack.ui.utils.createChip
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class SearchHistoryItemHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<SearchHistoryItem, SearchHistoryItemHolderBinding>(
    container,
    SearchHistoryItemHolderBinding::inflate
) {
    @Suppress("ComplexMethod")
    override fun SearchHistoryItemHolderBinding.bind(item: SearchHistoryItem) {
        val (query, isAccepted, minNumAnswers, bodyContains, isClosed, tags, titleContains) = item.searchPayload

        itemView.setThrottledOnClickListener {
            item.onPayloadReceived(item.searchPayload)
        }

        searchQuery.text = binding.searchQuery.context.getString(R.string.search_query, query)

        filtersView.removeAllViews()
        val addedFilters = listOf(
            isAccepted?.let { Filter.Accepted(it) } ?: Filter.None,
            minNumAnswers?.let { Filter.MinAnswers(it) } ?: Filter.None,
            bodyContains?.let { Filter.BodyContains(it) } ?: Filter.None,
            isClosed?.let { Filter.Closed(it) } ?: Filter.None,
            tags?.let { Filter.Tags(it) } ?: Filter.None,
            titleContains?.let { Filter.TitleContains(it) } ?: Filter.None
        ).filter { it != Filter.None }

        addedFilters
            .map { it.getLabel(filtersView.context) }
            .forEach { label -> filtersView.addView(filtersView.context.createChip(label)) }
    }
}
