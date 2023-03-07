package me.tylerbwong.stack.ui.profile

import android.view.ViewGroup
import androidx.core.view.isVisible
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.AnswerHolderBinding
import me.tylerbwong.stack.ui.home.AnswerItem
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class AnswerHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AnswerItem, AnswerHolderBinding>(
    container,
    AnswerHolderBinding::inflate
) {
    override fun AnswerHolderBinding.bind(item: AnswerItem) {
        questionTitle.setLatex(item.answer.title.toHtml().toString())
        ownerView.bind(item.answer.owner)
        commentCount.isVisible = item.answer.commentCount != null
        commentCount.text = item.answer.commentCount?.toLong()?.format()
        itemView.setThrottledOnClickListener {
            val intent = QuestionDetailActivity.makeIntent(
                context = it.context,
                questionId = item.answer.questionId,
                answerId = item.answer.answerId,
            )
            it.context.startActivity(intent)
        }
    }
}
