package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.soywiz.klock.seconds
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailHolderBinding
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionDetailHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<QuestionMainItem, QuestionDetailHolderBinding>(
    container,
    QuestionDetailHolderBinding::inflate
) {

    init {
        binding.questionBody.setSpannableFactory(noCopySpannableFactory)
    }

    override fun QuestionDetailHolderBinding.bind(item: QuestionMainItem) {
        val question = item.question
        questionTitle.text = question.title.toHtml()
        askedDate.apply {
            text = context.getString(
                R.string.asked,
                question.creationDate.seconds.millisecondsLong.formatElapsedTime(context)
            )
        }
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
            val chip = Chip(itemView.context).apply {
                text = it
                setThrottledOnClickListener { view ->
                    QuestionsActivity.startActivityForKey(view.context, TAGS, it)
                }
            }
            tagsView.addView(chip)
        }
    }
}
