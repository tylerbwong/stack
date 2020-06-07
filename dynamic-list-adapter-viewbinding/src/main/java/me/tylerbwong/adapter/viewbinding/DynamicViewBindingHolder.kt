package me.tylerbwong.adapter.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import me.tylerbwong.adapter.DynamicHolder
import me.tylerbwong.adapter.DynamicItem

abstract class DynamicViewBindingHolder<in T : DynamicItem, VB : ViewBinding>(
    container: ViewGroup,
    viewBindingProvider: (LayoutInflater, ViewGroup, Boolean) -> VB,
    protected val binding: VB = viewBindingProvider(LayoutInflater.from(container.context), container, false)
) : DynamicHolder<T>(binding.root) {

    abstract fun VB.bind(item: T)

    final override fun bind(item: T) = binding.bind(item)
}
