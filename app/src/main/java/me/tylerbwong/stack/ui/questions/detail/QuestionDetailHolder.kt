package me.tylerbwong.stack.ui.questions.detail

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailHolderBinding
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionDetailHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    internal val binding = QuestionDetailHolderBinding.bind(itemView)

    fun bind(data: QuestionItem) {
        val question = data.question
        binding.questionTitle.text = question.title.toHtml()
        binding.lastEditor.apply {
            isVisible = question.lastEditor != null
            text = context.getString(R.string.last_edited_by, question.lastEditor?.displayName)
        }

        binding.questionBody.apply {
            question.bodyMarkdown?.let { body ->
                setMarkdown(body)
            }
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }

        binding.ownerView.bind(question.owner, isInCurrentSite = data.isInCurrentSite)

        binding.tagsView.removeAllViews()
        question.tags?.forEach {
            val chip = Chip(itemView.context).apply {
                text = it
                if (data.isInCurrentSite) {
                    setThrottledOnClickListener { view ->
                        QuestionsActivity.startActivityForKey(view.context, TAGS, it)
                    }
                }
            }
            binding.tagsView.addView(chip)
        }
    }
}
