package me.tylerbwong.stack.ui.questions.detail

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.soywiz.klock.seconds
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailHolderBinding
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.createChip
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.ofType
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
            visibility = if (question.lastEditor != null) View.VISIBLE else View.INVISIBLE
            text = context.getString(R.string.last_edited_by, question.lastEditor?.displayName)
        }

        commentCount.apply {
            text = (question.commentCount ?: 0).toLong().format()
            setOnClickListener {
                it.context.ofType<FragmentActivity>()?.let { activity ->
                    CommentsBottomSheetDialogFragment.show(
                        activity.supportFragmentManager,
                        question.questionId
                    )
                }
            }
        }

        question.bodyMarkdown?.let { body ->
            questionBody.setMarkdown(body)
        }

        ownerView.bind(question.owner)

        tagsView.removeAllViews()
        question.tags?.forEach { tag ->
            tagsView.addView(
                itemView.context.createChip(tag) {
                    QuestionsActivity.startActivityForKey(it.context, TAGS, tag)
                }
            )
        }
    }
}
