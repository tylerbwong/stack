package me.tylerbwong.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.core.util.set
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

typealias ViewHolderProvider = (ViewGroup) -> DynamicHolder<*>

private val noOpDiffUtilCallback = object : DiffUtil.ItemCallback<DynamicItem>() {
    override fun areItemsTheSame(oldItem: DynamicItem, newItem: DynamicItem) = false
    override fun areContentsTheSame(oldItem: DynamicItem, newItem: DynamicItem) = false
}

private val noOpAsyncDifferConfig = AsyncDifferConfig.Builder(noOpDiffUtilCallback).build()

class DynamicListAdapter : ListAdapter<DynamicItem, DynamicHolder<*>> {

    private val viewHolderProviders = SparseArray<ViewHolderProvider>()

    constructor(diffUtilCallback: DiffUtil.ItemCallback<DynamicItem> = noOpDiffUtilCallback) : super(diffUtilCallback)
    constructor(asyncDifferConfig: AsyncDifferConfig<DynamicItem> = noOpAsyncDifferConfig) : super(asyncDifferConfig)

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

    override fun getItemId(position: Int): Long = getItem(position).itemId.toLong()
}
