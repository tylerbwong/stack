package me.tylerbwong.stack.ui.home

import android.view.ViewGroup
import me.tylerbwong.stack.databinding.SectionHeaderHolderBinding
import me.tylerbwong.stack.ui.adapter.ViewBindingViewHolder

class SectionHeaderHolder(
    container: ViewGroup
) : ViewBindingViewHolder<SectionHeaderItem, SectionHeaderHolderBinding>(
    container,
    SectionHeaderHolderBinding::inflate
) {
    override fun SectionHeaderHolderBinding.bind(item: SectionHeaderItem) {
        sectionHeader.text = item.header
    }
}
