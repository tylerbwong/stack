package me.tylerbwong.stack.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.HeaderViewHolder
import me.tylerbwong.stack.ui.drafts.AnswerDraftHolder
import me.tylerbwong.stack.ui.questions.QuestionViewHolder
import me.tylerbwong.stack.ui.utils.inflate

class HomeAdapter : ListAdapter<HomeItem, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder<HomeItem>(HomeItemDiffCallback()).build()
) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderItem -> ITEM_TYPE_HEADER
        is QuestionItem -> ITEM_TYPE_QUESTION
        is AnswerDraftItem -> ITEM_TYPE_ANSWER_DRAFT
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ITEM_TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.header_holder))
        ITEM_TYPE_QUESTION -> QuestionViewHolder(parent.inflate(R.layout.question_holder))
        else -> AnswerDraftHolder(parent.inflate(R.layout.answer_draft_holder))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when {
            holder is HeaderViewHolder && item is HeaderItem -> holder.bind(item)
            holder is QuestionViewHolder && item is QuestionItem -> holder.bind(item.question)
            holder is AnswerDraftHolder && item is AnswerDraftItem -> holder.bind(item)
        }
    }

    companion object {
        private const val ITEM_TYPE_HEADER = 1
        private const val ITEM_TYPE_QUESTION = 2
        private const val ITEM_TYPE_ANSWER_DRAFT = 3
    }
}
