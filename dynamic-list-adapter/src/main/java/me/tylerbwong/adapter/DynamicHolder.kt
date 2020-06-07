package me.tylerbwong.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DynamicHolder<T : DynamicItem>(container: View) : RecyclerView.ViewHolder(container) {
    abstract fun bind(item: T)
}
