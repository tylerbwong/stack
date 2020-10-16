package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class AskedHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AskedItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: AskedItem) {
        title.text = item.event.timelineType
    }
}
