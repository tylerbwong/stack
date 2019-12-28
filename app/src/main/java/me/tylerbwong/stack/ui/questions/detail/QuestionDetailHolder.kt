package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.question_detail_holder.*
import kotlinx.android.synthetic.main.question_detail_holder.view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionDetailHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.question_detail_holder).also {
        it.questionBody.setSpannableFactory(noCopySpannableFactory)
    }
) {
    override fun bind(data: Any) {
        (data as? QuestionDetailDataModel)?.let { dataModel ->
            questionTitle.text = dataModel.questionTitle.toHtml()
            lastEditor.apply {
                isVisible = dataModel.lastEditorName != null
                text = context.getString(R.string.last_edited_by, dataModel.lastEditorName)
            }

            questionBody.apply {
                dataModel.questionBody?.let { body ->
                    setMarkdown(body)
                }
                setTextIsSelectable(true)
                movementMethod = BetterLinkMovementMethod.getInstance()
            }

            ownerView.bind(dataModel.owner)

            tagsView.removeAllViews()
            dataModel.tags?.forEach {
                val chip = Chip(tagsView.context).apply {
                    text = it
                    setThrottledOnClickListener { view ->
                        QuestionsActivity.startActivityForKey(view.context, TAGS, it)
                    }
                }
                tagsView.addView(chip)
            }
        }
    }
}
