package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class CommentedHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<CommentedItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: CommentedItem) {
        title.text = item.event.timelineType
    }
}
