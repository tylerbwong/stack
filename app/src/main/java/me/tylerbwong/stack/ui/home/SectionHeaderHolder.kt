package me.tylerbwong.stack.ui.home

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.SectionHeaderHolderBinding

class SectionHeaderHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<SectionHeaderItem, SectionHeaderHolderBinding>(
    container,
    SectionHeaderHolderBinding::inflate
) {
    override fun SectionHeaderHolderBinding.bind(item: SectionHeaderItem) {
        sectionHeader.text = item.header
    }
}
