package me.tylerbwong.stack.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.header_holder.*

class HeaderViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(headerItem: HeaderItem) {
        title.text = headerItem.title
        subtitle.text = headerItem.subtitle
    }
}
