package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class AnsweredHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AnsweredItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: AnsweredItem) {
        title.text = item.event.timelineType
    }
}
