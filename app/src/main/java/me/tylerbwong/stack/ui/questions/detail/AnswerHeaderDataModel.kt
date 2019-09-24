package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import kotlinx.android.synthetic.main.answer_header.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicDataModel
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflate

data class AnswerHeaderDataModel(internal val answerCount: Int) : DynamicDataModel() {

    override fun areItemsThemSame(other: DynamicDataModel) = other == this

    override fun areContentsTheSame(other: DynamicDataModel) = other == this

    override fun getViewCreator() = ::AnswerHeaderViewHolder
}

class AnswerHeaderViewHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.answer_header)
) {
    override fun bind(data: Any) {
        (data as? AnswerHeaderDataModel)?.let {
            answersCount.text = answersCount.context.resources.getQuantityString(
                R.plurals.answers,
                it.answerCount,
                it.answerCount
            )
        }
    }
}
