package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.SpacerHolderBinding

class SpacerHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<SpacerItem, SpacerHolderBinding>(
    container,
    SpacerHolderBinding::inflate
) {
    override fun SpacerHolderBinding.bind(item: SpacerItem) {
        // No-op not needed
    }
}
