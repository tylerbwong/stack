package me.tylerbwong.stack.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.search_history_item_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.home.SearchHistoryItem
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class SearchHistoryItemHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    @Suppress("ComplexMethod")
    fun bind(item: SearchHistoryItem) {
        val payload = item.searchPayload as? SearchPayload.Standard ?: return
        val (query, isAccepted, minNumAnswers, bodyContains, isClosed, tags, titleContains) = payload

        containerView.setThrottledOnClickListener {
            item.onPayloadReceived(item.searchPayload)
        }

        searchQuery.text = searchQuery.context.getString(R.string.search_query, query)

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
            .forEach { label ->
                filtersView.addView(
                    Chip(filtersView.context).apply {
                        text = label
                    }
                )
            }
    }
}
