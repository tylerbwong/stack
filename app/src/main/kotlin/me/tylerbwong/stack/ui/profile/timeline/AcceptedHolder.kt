package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class AcceptedHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AcceptedItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: AcceptedItem) {
        title.text = item.event.timelineType
    }
}
