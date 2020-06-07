package me.tylerbwong.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.core.util.set
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

typealias ViewHolderProvider = (ViewGroup) -> DynamicHolder<*>

class DynamicListAdapter(
    diffUtilCallback: DiffUtil.ItemCallback<DynamicItem>
) : ListAdapter<DynamicItem, DynamicHolder<*>>(diffUtilCallback) {
    private val viewHolderProviders = SparseArray<ViewHolderProvider>()

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val itemId = item.itemId
        viewHolderProviders[itemId] = item.viewHolderProvider
        return itemId
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = viewHolderProviders[viewType].invoke(parent)

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: DynamicHolder<*>,
        position: Int
    ) = (holder as DynamicHolder<DynamicItem>).bind(getItem(position))
}
