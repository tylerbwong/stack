package me.tylerbwong.stack.ui.questions

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching

class HeaderHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.header_holder)
) {

    private val title: TextView = ViewCompat.requireViewById(itemView, R.id.title)
    private val subtitle: TextView = ViewCompat.requireViewById(itemView, R.id.subtitle)

    override fun bind(data: Any) {
        (data as? HeaderDataModel)?.let {
            title.text = it.title
            subtitle.text = it.subtitle
        }
    }
}