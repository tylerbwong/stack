package me.tylerbwong.stack.ui.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionHolderBinding
import me.tylerbwong.stack.ui.adapter.DelegatedItem
import me.tylerbwong.stack.ui.adapter.ViewBindingViewHolder
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.systemService
import me.tylerbwong.stack.ui.utils.toHtml

object QuestionItemCallback : DiffUtil.ItemCallback<DelegatedItem>() {
    override fun areItemsTheSame(
        oldItem: DelegatedItem,
        newItem: DelegatedItem
    ) = oldItem is QuestionItem && newItem is QuestionItem &&
            oldItem.question.questionId == newItem.question.questionId

    override fun areContentsTheSame(
        oldItem: DelegatedItem,
        newItem: DelegatedItem
    ) = oldItem is QuestionItem && newItem is QuestionItem &&
            oldItem.question.title == newItem.question.title &&
            oldItem.question.answerCount == newItem.question.answerCount &&
            oldItem.question.owner == newItem.question.owner
}

class QuestionViewHolder(
    container: ViewGroup
) : ViewBindingViewHolder<QuestionItem, QuestionHolderBinding>(
    container,
    QuestionHolderBinding::inflate
) {
    override fun QuestionHolderBinding.bind(item: QuestionItem) {
        val question = item.question
        questionTitle.text = question.title.toHtml()
        answerCount.text = question.answerCount.toString()

        ownerView.bind(question.owner)

        itemView.setOnLongClickListener { view ->
            val context = view.context
            context.systemService<ClipboardManager>(Context.CLIPBOARD_SERVICE)?.let {
                it.setPrimaryClip(ClipData.newPlainText(LABEL, question.shareLink))
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
