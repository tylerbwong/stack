package me.tylerbwong.stack.ui.drafts

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.answer_draft_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.home.AnswerDraftItem
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class AnswerDraftHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(data: AnswerDraftItem) {
        val draft = data.draft
        questionTitle.text = draft.questionTitle
        timestamp.text = itemView.context.getString(R.string.last_updated, draft.formattedTimestamp)
        draftPreview.setMarkdown(draft.bodyMarkdown)

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
