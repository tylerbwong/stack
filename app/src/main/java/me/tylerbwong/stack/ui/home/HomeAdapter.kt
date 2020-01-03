package me.tylerbwong.stack.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.QuestionViewHolder
import me.tylerbwong.stack.ui.utils.inflate

class HomeAdapter : ListAdapter<HomeItem, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder<HomeItem>(HomeItemDiffCallback()).build()
) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderItem -> ITEM_TYPE_HEADER
        is QuestionItem -> ITEM_TYPE_QUESTION
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ITEM_TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.header_holder))
        else -> QuestionViewHolder(parent.inflate(R.layout.question_holder))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when {
            holder is HeaderViewHolder && item is HeaderItem -> holder.bind(item)
            holder is QuestionViewHolder && item is QuestionItem -> holder.bind(item.question)
        }
    }

    companion object {
        private const val ITEM_TYPE_HEADER = 1
        private const val ITEM_TYPE_QUESTION = 2
    }
}
