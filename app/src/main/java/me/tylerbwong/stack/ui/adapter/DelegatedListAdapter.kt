package me.tylerbwong.stack.ui.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.set
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

typealias ViewHolderProvider = (ViewGroup) -> ViewBindingViewHolder<*, *>

class DelegatedListAdapter(
    diffUtilCallback: DiffUtil.ItemCallback<DelegatedItem>
) : ListAdapter<DelegatedItem, ViewBindingViewHolder<*, *>>(diffUtilCallback) {
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
        holder: ViewBindingViewHolder<*, *>,
        position: Int
    ) = (holder as ViewBindingViewHolder<DelegatedItem, ViewBinding>).bindInternal(getItem(position))
}

abstract class DelegatedItem(
    val viewHolderProvider: ViewHolderProvider
) {
    internal val itemId = this::class.java.name.hashCode()
}

abstract class ViewBindingViewHolder<T : DelegatedItem, VB : ViewBinding>(
    container: ViewGroup,
    viewBindingProvider: (LayoutInflater, ViewGroup, Boolean) -> VB,
    protected val binding: VB = viewBindingProvider(LayoutInflater.from(container.context), container, false)
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun VB.bind(item: T)

    internal fun bindInternal(item: T) = binding.bind(item)
}
