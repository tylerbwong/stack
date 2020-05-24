package me.tylerbwong.stack.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.section_header_holder.*

class SectionHeaderHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: SectionHeaderItem) {
        sectionHeader.text = item.header
    }
}
