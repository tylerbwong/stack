package me.tylerbwong.stack.ui.questions.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.answer_header.*
import me.tylerbwong.stack.R

class AnswerHeaderViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(data: AnswerHeaderItem) {
        answersCount.text = answersCount.context.resources.getQuantityString(
            R.plurals.answers,
            data.answerCount,
            data.answerCount
        )
    }
}
