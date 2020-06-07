package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerHeaderBinding
import me.tylerbwong.stack.ui.adapter.ViewBindingViewHolder

class AnswerHeaderViewHolder(
    container: ViewGroup
) : ViewBindingViewHolder<AnswerHeaderItem, AnswerHeaderBinding>(
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
