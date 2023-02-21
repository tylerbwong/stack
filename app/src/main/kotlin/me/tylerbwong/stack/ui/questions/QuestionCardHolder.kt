package me.tylerbwong.stack.ui.questions

import android.view.ViewGroup
import android.widget.Toast
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionCardBinding
import me.tylerbwong.stack.ui.home.QuestionCardItem
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.copyToClipboard
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class QuestionCardHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<QuestionCardItem, QuestionCardBinding>(
    container,
    QuestionCardBinding::inflate,
) {
    override fun QuestionCardBinding.bind(item: QuestionCardItem) {
        val question = item.question
        questionTitle.setLatex(question.title)
        itemView.setOnLongClickListener { view ->
            val context = view.context
            val isCopied = context.copyToClipboard(QuestionHolder.LABEL, question.shareLink)
            if (isCopied) {
                Toast.makeText(context, R.string.link_copied, Toast.LENGTH_SHORT).show()
            }
            true
        }

        itemView.setThrottledOnClickListener {
            QuestionDetailActivity.startActivity(it.context, question.questionId)
        }
    }
}
