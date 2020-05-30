package me.tylerbwong.stack.ui

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.databinding.HeaderHolderBinding
import me.tylerbwong.stack.ui.home.HeaderItem

class HeaderViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = HeaderHolderBinding.bind(itemView)

    fun bind(headerItem: HeaderItem) {
        binding.title.text = headerItem.title
        binding.subtitle.isVisible = headerItem.subtitle != null
        binding.subtitle.text = headerItem.subtitle
    }
}
