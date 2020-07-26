package me.tylerbwong.stack.ui.search.filters

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.FilterInputHolderBinding
import me.tylerbwong.stack.ui.home.FilterInputItem
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.ofType
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class FilterInputHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<FilterInputItem, FilterInputHolderBinding>(
    container,
    FilterInputHolderBinding::inflate
) {
    override fun FilterInputHolderBinding.bind(item: FilterInputItem) {
        advancedOptions.removeAllViews()

        val payload = item.searchPayload

        // Add persistent filter button chip
        advancedOptions.addView(
            advancedOptions.inflate<Chip>(R.layout.filter_chip_button).apply {
                setThrottledOnClickListener { view ->
                    view.context.ofType<AppCompatActivity>()?.let { activity ->
                        FilterBottomSheetDialogFragment.show(
                            activity.supportFragmentManager,
                            payload
                        ) { item.onPayloadReceived(it) }
                    }
                }
            }
        )
        setFilters(item)
    }

    @Suppress("ComplexMethod")
    private fun FilterInputHolderBinding.setFilters(item: FilterInputItem) {
        val payload = item.searchPayload
        val (_, isAccepted, minNumAnswers, bodyContains, isClosed, tags, titleContains) = payload

        val enabledFilters = listOf(
            isAccepted?.let { Filter.Accepted(it) } ?: Filter.None,
            minNumAnswers?.let { Filter.MinAnswers(it) } ?: Filter.None,
            bodyContains?.let { Filter.BodyContains(it) } ?: Filter.None,
            isClosed?.let { Filter.Closed(it) } ?: Filter.None,
            tags?.let { Filter.Tags(it) } ?: Filter.None,
            titleContains?.let { Filter.TitleContains(it) } ?: Filter.None
        ).filter { it != Filter.None }

        // Add enabled filters
        enabledFilters
            .map { it.getLabel(advancedOptions.context) }
            .forEach { label ->
                advancedOptions.addView(
                    advancedOptions.inflate<Chip>(R.layout.advanced_filter_chip).apply {
                        text = label
                        setThrottledOnClickListener { view ->
                            view.context.ofType<AppCompatActivity>()?.let { activity ->
                                FilterBottomSheetDialogFragment.show(
                                    activity.supportFragmentManager,
                                    payload
                                ) { item.onPayloadReceived(it) }
                            }
                        }
                        setOnCloseIconClickListener {
                            val removedFilter = enabledFilters.firstOrNull { filter ->
                                label in filter.getLabel(it.context)
                            } ?: Filter.None
                            val newPayload = when (removedFilter) {
                                is Filter.Accepted -> payload.copy(isAccepted = null)
                                is Filter.MinAnswers -> payload.copy(minNumAnswers = null)
                                is Filter.BodyContains -> payload.copy(bodyContains = null)
                                is Filter.Closed -> payload.copy(isClosed = null)
                                is Filter.Tags -> payload.copy(tags = null)
                                is Filter.TitleContains -> payload.copy(titleContains = null)
                                is Filter.None -> payload.copy()
                            }
                            item.onPayloadReceived(newPayload)
                        }
                    }
                )
            }
    }
}
