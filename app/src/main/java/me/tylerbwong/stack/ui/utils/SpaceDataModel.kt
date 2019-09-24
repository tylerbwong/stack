package me.tylerbwong.stack.ui.utils

import android.view.ViewGroup
import me.tylerbwong.stack.R

class SpaceDataModel : DynamicDataModel() {
    override fun getViewCreator() = ::SpaceHolder
}

class SpaceHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.empty_space)
) {
    override fun bind(data: Any) {
        // No-op
    }
}
