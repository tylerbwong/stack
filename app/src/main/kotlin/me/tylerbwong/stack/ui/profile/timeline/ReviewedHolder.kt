package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class ReviewedHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<ReviewedItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: ReviewedItem) {
        title.text = item.event.timelineType
    }
}
