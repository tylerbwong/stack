package me.tylerbwong.stack.ui

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.header_holder.*
import me.tylerbwong.stack.ui.home.HeaderItem

class HeaderViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(headerItem: HeaderItem) {
        title.text = headerItem.title
        subtitle.isVisible = headerItem.subtitle != null
        subtitle.text = headerItem.subtitle
    }
}
