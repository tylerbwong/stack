package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.DividerItemBinding

class DividerHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<DividerItem, DividerItemBinding>(
    container,
    DividerItemBinding::inflate,
) {
    override fun DividerItemBinding.bind(item: DividerItem) = Unit
}
