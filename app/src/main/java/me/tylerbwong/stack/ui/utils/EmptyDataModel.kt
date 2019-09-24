package me.tylerbwong.stack.ui.utils

import android.view.ViewGroup
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.empty_layout.*
import me.tylerbwong.stack.R

class EmptyDataModel(@StringRes internal val messageId: Int) : DynamicDataModel() {

    override fun areItemsThemSame(
        other: DynamicDataModel
    ) = other is EmptyDataModel && other.messageId == messageId

    override fun getViewCreator() = ::EmptyHolder
}

class EmptyHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.empty_layout)
) {
    override fun bind(data: Any) {
        (data as? EmptyDataModel)?.let {
            message.setText(it.messageId)
        }
    }
}
