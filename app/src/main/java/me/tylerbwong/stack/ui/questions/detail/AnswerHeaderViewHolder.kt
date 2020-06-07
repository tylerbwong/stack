package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerHeaderBinding

class AnswerHeaderViewHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AnswerHeaderItem, AnswerHeaderBinding>(
    container,
    AnswerHeaderBinding::inflate
) {
    override fun AnswerHeaderBinding.bind(item: AnswerHeaderItem) {
        binding.answersCount.text = itemView.context.resources.getQuantityString(
            R.plurals.answers,
            item.answerCount,
            item.answerCount
        )
    }
}
