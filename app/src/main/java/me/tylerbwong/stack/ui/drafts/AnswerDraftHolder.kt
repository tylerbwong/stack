package me.tylerbwong.stack.ui.drafts

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerDraftHolderBinding
import me.tylerbwong.stack.ui.home.AnswerDraftItem
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class AnswerDraftHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = AnswerDraftHolderBinding.bind(itemView)

    fun bind(data: AnswerDraftItem) {
        val draft = data.draft
        binding.questionTitle.text = draft.questionTitle.toHtml()
        binding.timestamp.text = itemView.context.getString(R.string.last_updated, draft.formattedTimestamp)
        binding.draftPreview.setMarkdown(draft.bodyMarkdown)

        itemView.setThrottledOnClickListener {
            val intent = QuestionDetailActivity.makeIntent(
                context = it.context,
                id = draft.questionId,
                isInAnswerMode = true
            )
            it.context.startActivity(intent)
        }
    }
}
