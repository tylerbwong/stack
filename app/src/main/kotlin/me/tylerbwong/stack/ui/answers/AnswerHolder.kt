package me.tylerbwong.stack.ui.answers

import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.soywiz.klock.seconds
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerHolderBinding
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.questions.detail.AnswerItem
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.ofType
import me.tylerbwong.stack.ui.utils.toHtml

class AnswerHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AnswerItem, AnswerHolderBinding>(
    container,
    AnswerHolderBinding::inflate
) {

    init {
        binding.answerBody.setSpannableFactory(noCopySpannableFactory)
    }

    override fun AnswerHolderBinding.bind(item: AnswerItem) {
        val answer = item.answer
        val voteCount = answer.upVoteCount - answer.downVoteCount
        votes.text =
            itemView.context.resources.getQuantityString(R.plurals.votes, voteCount, voteCount)
        acceptedAnswerCheck.isVisible = answer.isAccepted

        answerBody.setMarkdown(answer.bodyMarkdown)

        answeredDate.apply {
            text = context.getString(
                R.string.answered,
                answer.creationDate.seconds.millisecondsLong.formatElapsedTime(context)
            )
        }

        lastEditor.apply {
            isInvisible = answer.lastEditor == null
            text = context.getString(
                R.string.last_edited_by,
                answer.lastEditor?.displayName?.toHtml()?.toString()
            )
        }

        commentCount.apply {
            text = (answer.commentCount ?: 0).toLong().format()
            setOnClickListener {
                it.context.ofType<FragmentActivity>()?.let { activity ->
                    CommentsBottomSheetDialogFragment.show(
                        activity.supportFragmentManager,
                        answer.answerId
                    )
                }
            }
        }

        ownerView.bind(answer.owner)
    }
}
