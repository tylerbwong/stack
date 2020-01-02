package me.tylerbwong.stack.ui.home

import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.stack.data.model.Question

sealed class HomeItem
data class HeaderItem(val title: String, val subtitle: String) : HomeItem()
data class QuestionItem(val question: Question) : HomeItem()

class HomeItemDiffCallback : DiffUtil.ItemCallback<HomeItem>() {
    override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem) =
        oldItem.javaClass == newItem.javaClass &&
                (oldItem is HeaderItem || oldItem is QuestionItem && newItem is QuestionItem &&
                        oldItem.question.questionId == newItem.question.questionId)

    override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem) = when {
        oldItem is HeaderItem && newItem is HeaderItem -> oldItem == newItem
        oldItem is QuestionItem && newItem is QuestionItem ->
            oldItem.question.title == newItem.question.title &&
                    oldItem.question.answerCount == newItem.question.answerCount &&
                    oldItem.question.owner == newItem.question.owner
        else -> false
    }
}
