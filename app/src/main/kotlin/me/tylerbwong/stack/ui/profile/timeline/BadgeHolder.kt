package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class BadgeHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<BadgeItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: BadgeItem) {
        title.text = item.event.timelineType
    }
}
