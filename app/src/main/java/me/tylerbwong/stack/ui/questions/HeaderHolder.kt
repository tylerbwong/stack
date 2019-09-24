package me.tylerbwong.stack.ui.questions

import android.view.ViewGroup
import kotlinx.android.synthetic.main.header_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflate

class HeaderHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.header_holder)
) {
    override fun bind(data: Any) {
        (data as? HeaderDataModel)?.let {
            title.text = it.title
            subtitle.text = it.subtitle
        }
    }
}
