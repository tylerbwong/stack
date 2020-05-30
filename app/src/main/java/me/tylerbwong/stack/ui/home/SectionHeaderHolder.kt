package me.tylerbwong.stack.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.databinding.SectionHeaderHolderBinding

class SectionHeaderHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = SectionHeaderHolderBinding.bind(itemView)

    fun bind(item: SectionHeaderItem) {
        binding.sectionHeader.text = item.header
    }
}
