package me.tylerbwong.stack.ui.questions

import android.view.ViewGroup
import kotlinx.android.synthetic.main.header_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching

class HeaderHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.header_holder)
) {
    override fun bind(data: Any) {
        (data as? HeaderDataModel)?.let {
            title.text = it.title
            subtitle.text = it.subtitle
        }
    }
}
