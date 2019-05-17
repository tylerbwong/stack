package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicDataModel
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching

data class AnswerHeaderDataModel(internal val answerCount: Int) : DynamicDataModel() {

    override fun areItemsThemSame(other: DynamicDataModel) = other == this

    override fun getViewCreator() = ::AnswerHeaderViewHolder
}

class AnswerHeaderViewHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.answer_header)
) {
    private val answersCountText = ViewCompat.requireViewById<TextView>(itemView, R.id.answersCount)

    override fun bind(data: Any) {
        (data as? AnswerHeaderDataModel)?.let {
            answersCountText.text = answersCountText.context.resources.getQuantityString(
                    R.plurals.answers,
                    it.answerCount,
                    it.answerCount
            )
        }
    }
}
