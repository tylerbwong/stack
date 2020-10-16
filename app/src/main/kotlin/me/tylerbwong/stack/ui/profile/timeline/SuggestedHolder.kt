package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class SuggestedHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<SuggestedItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: SuggestedItem) {
        title.text = item.event.timelineType
    }
}
