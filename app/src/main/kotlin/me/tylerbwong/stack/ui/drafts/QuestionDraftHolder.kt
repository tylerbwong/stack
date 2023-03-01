package me.tylerbwong.stack.ui.drafts

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.DraftHolderBinding
import me.tylerbwong.stack.ui.home.QuestionDraftItem
import me.tylerbwong.stack.ui.questions.ask.AskQuestionActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionDraftHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<QuestionDraftItem, DraftHolderBinding>(
    container,
    DraftHolderBinding::inflate
) {
    override fun DraftHolderBinding.bind(item: QuestionDraftItem) {
        val draft = item.draft
        questionTitle.text = draft.title.toHtml()
        timestamp.text = itemView.context.getString(R.string.last_updated, draft.formattedTimestamp)
        draftPreview.setMarkdown(draft.body)
        draftPreview.movementMethod = null

        itemView.setThrottledOnClickListener {
            AskQuestionActivity.startActivity(it.context, draft.id)
        }
    }
}
