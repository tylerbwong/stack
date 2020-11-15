package me.tylerbwong.stack.ui.questions

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionHolderBinding
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.copyToClipboard
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

object QuestionItemCallback : DiffUtil.ItemCallback<DynamicItem>() {
    override fun areItemsTheSame(
        oldItem: DynamicItem,
        newItem: DynamicItem
    ) = oldItem is QuestionItem && newItem is QuestionItem &&
            oldItem.question.questionId == newItem.question.questionId

    override fun areContentsTheSame(
        oldItem: DynamicItem,
        newItem: DynamicItem
    ) = oldItem is QuestionItem && newItem is QuestionItem &&
            oldItem.question.title == newItem.question.title &&
            oldItem.question.answerCount == newItem.question.answerCount &&
            oldItem.question.owner == newItem.question.owner
}

class QuestionHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<QuestionItem, QuestionHolderBinding>(
    container,
    QuestionHolderBinding::inflate
) {
    override fun QuestionHolderBinding.bind(item: QuestionItem) {
        val question = item.question
        questionTitle.setLatex(question.title.toHtml().toString())
        answerCount.text = question.answerCount.toString()

        ownerView.bind(question.owner)

        itemView.setOnLongClickListener { view ->
            val context = view.context
            val isCopied = context.copyToClipboard(LABEL, question.shareLink)
            if (isCopied) {
                Toast.makeText(context, R.string.link_copied, Toast.LENGTH_SHORT).show()
            }
            true
        }

        itemView.setThrottledOnClickListener {
            QuestionDetailActivity.startActivity(it.context, question.questionId)
        }
    }

    companion object {
        private const val LABEL = "linkText"
    }
}
