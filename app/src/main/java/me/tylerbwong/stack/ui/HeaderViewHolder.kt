package me.tylerbwong.stack.ui

import android.view.ViewGroup
import androidx.core.view.isVisible
import me.tylerbwong.stack.databinding.HeaderHolderBinding
import me.tylerbwong.stack.ui.adapter.ViewBindingViewHolder
import me.tylerbwong.stack.ui.home.HeaderItem

class HeaderViewHolder(
    container: ViewGroup
) : ViewBindingViewHolder<HeaderItem, HeaderHolderBinding>(container, HeaderHolderBinding::inflate) {
    override fun HeaderHolderBinding.bind(item: HeaderItem) {
        title.text = item.title
        subtitle.isVisible = item.subtitle != null
        subtitle.text = item.subtitle
    }
}
