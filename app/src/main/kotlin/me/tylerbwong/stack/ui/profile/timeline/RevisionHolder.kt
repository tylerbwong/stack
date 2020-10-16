package me.tylerbwong.stack.ui.profile.timeline

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.HeaderHolderBinding

class RevisionHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<RevisionItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: RevisionItem) {
        title.text = item.event.timelineType
    }
}
