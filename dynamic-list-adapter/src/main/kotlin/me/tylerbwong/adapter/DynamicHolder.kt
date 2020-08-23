package me.tylerbwong.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DynamicHolder<in T : DynamicItem>(rootView: View) : RecyclerView.ViewHolder(rootView) {
    abstract fun bind(item: T)
}
