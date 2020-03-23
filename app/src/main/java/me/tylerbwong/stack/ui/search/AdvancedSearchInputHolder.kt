package me.tylerbwong.stack.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.advanced_search_input_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.home.AdvancedSearchInputItem
import me.tylerbwong.stack.ui.utils.inflate

class AdvancedSearchInputHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    @Suppress("ComplexMethod")
    fun bind(item: AdvancedSearchInputItem) {
        val advancedPayload = item.searchPayload as? SearchPayload.Advanced ?: return
        val (_, isAccepted, minNumAnswers, bodyContains, isClosed, tags, titleContains) = advancedPayload

        advancedOptions.removeAllViews()
        val addedFilters = listOf(
            isAccepted?.let { AdvancedFilter.Accepted(it) } ?: AdvancedFilter.None,
            minNumAnswers?.let { AdvancedFilter.MinAnswers(it) } ?: AdvancedFilter.None,
            bodyContains?.let { AdvancedFilter.BodyContains(it) } ?: AdvancedFilter.None,
            isClosed?.let { AdvancedFilter.Closed(it) } ?: AdvancedFilter.None,
            tags?.let { AdvancedFilter.Tags(it) } ?: AdvancedFilter.None,
            titleContains?.let { AdvancedFilter.TitleContains(it) } ?: AdvancedFilter.None
        ).filter { it != AdvancedFilter.None }

        addedFilters
            .map { it.getLabel(advancedOptions.context) }
            .forEach { label ->
                advancedOptions.addView(
                    advancedOptions.inflate<Chip>(R.layout.advanced_filter_chip).apply {
                        text = label
                        setOnCloseIconClickListener {
                            val removedFilter = addedFilters.firstOrNull { filter ->
                                label in filter.getLabel(it.context)
                            } ?: AdvancedFilter.None
                            val newPayload = when (removedFilter) {
                                is AdvancedFilter.Accepted -> advancedPayload.copy(isAccepted = null)
                                is AdvancedFilter.MinAnswers -> advancedPayload.copy(minNumAnswers = null)
                                is AdvancedFilter.BodyContains -> advancedPayload.copy(bodyContains = null)
                                is AdvancedFilter.Closed -> advancedPayload.copy(isClosed = null)
                                is AdvancedFilter.Tags -> advancedPayload.copy(tags = null)
                                is AdvancedFilter.TitleContains -> advancedPayload.copy(
                                    titleContains = null
                                )
                                is AdvancedFilter.None -> advancedPayload.copy()
                            }
                            item.onPayloadReceived(newPayload)
                        }
                    }
                )
            }
    }
}
