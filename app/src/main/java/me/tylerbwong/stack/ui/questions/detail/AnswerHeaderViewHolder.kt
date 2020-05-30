package me.tylerbwong.stack.ui.questions.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerHeaderBinding

class AnswerHeaderViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = AnswerHeaderBinding.bind(itemView)

    fun bind(data: AnswerHeaderItem) {
        binding.answersCount.text = itemView.context.resources.getQuantityString(
            R.plurals.answers,
            data.answerCount,
            data.answerCount
        )
    }
}
