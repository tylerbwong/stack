package me.tylerbwong.stack.ui.questions.detail

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.question_detail_holder.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionDetailHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(data: QuestionItem) {
        val question = data.question
        questionTitle.text = question.title.toHtml()
        lastEditor.apply {
            isVisible = question.lastEditor != null
            text = context.getString(R.string.last_edited_by, question.lastEditor?.displayName)
        }

        questionBody.apply {
            question.bodyMarkdown?.let { body ->
                setMarkdown(body)
            }
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }

        ownerView.bind(question.owner)

        tagsView.removeAllViews()
        question.tags?.forEach {
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
