package me.tylerbwong.stack.ui.utils

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

typealias ViewCreator = (ViewGroup) -> DynamicViewHolder

class DynamicViewAdapter : RecyclerView.Adapter<DynamicViewHolder>() {

    private var data = listOf<DynamicDataModel>()
    private val viewCreators = SparseArray<ViewCreator>()

    override fun getItemViewType(position: Int): Int {
        val currentData = data[position]
        val viewType = currentData.viewType

        if (viewCreators.get(viewType) == null) {
            viewCreators.put(viewType, currentData.getViewCreator())
        }

        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = viewCreators.get(viewType).invoke(parent)

    override fun onBindViewHolder(holder: DynamicViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    fun update(newData: List<DynamicDataModel>) {
        val oldData = ArrayList<DynamicDataModel>(data)
        data = newData
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
            ) = oldData[oldItemPosition].areItemsThemSame(newData[newItemPosition])

            override fun getOldListSize() = oldData.size

            override fun getNewListSize() = newData.size

            override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
            ) = oldData[oldItemPosition].areContentsTheSame(newData[newItemPosition])
        })
        diffResult.dispatchUpdatesTo(this)
    }
}

abstract class DynamicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // TODO: THIS IS BAD - Figure out a better way to do this
    abstract fun bind(data: Any)
}

abstract class DynamicDataModel {
    val viewType: Int
        get() = getViewCreator().hashCode()

    abstract fun getViewCreator(): ViewCreator

    open fun areItemsThemSame(other: DynamicDataModel): Boolean = false

    open fun areContentsTheSame(other: DynamicDataModel): Boolean = false
}
