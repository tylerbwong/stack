package me.tylerbwong.stack.ui.answers

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerHolderBinding
import me.tylerbwong.stack.ui.adapter.ViewBindingViewHolder
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.questions.detail.AnswerItem
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.ofType

class AnswerHolder(
    container: ViewGroup
) : ViewBindingViewHolder<AnswerItem, AnswerHolderBinding>(container, AnswerHolderBinding::inflate) {

    init {
        binding.answerBody.setSpannableFactory(noCopySpannableFactory)
    }

    override fun AnswerHolderBinding.bind(item: AnswerItem) {
        val answer = item.answer
        val voteCount = answer.upVoteCount - answer.downVoteCount
        votes.text =
            itemView.context.resources.getQuantityString(R.plurals.votes, voteCount, voteCount)
        acceptedAnswerCheck.isVisible = answer.isAccepted

        answerBody.apply {
            setMarkdown(answer.bodyMarkdown)
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }

        lastEditor.apply {
            isVisible = answer.lastEditor != null
            text = context.getString(R.string.last_edited_by, answer.lastEditor?.displayName)
        }

        ownerView.bind(answer.owner)

        itemView.setOnLongClickListener {
            it.context.ofType<FragmentActivity>()?.let { activity ->
                CommentsBottomSheetDialogFragment.show(
                    activity.supportFragmentManager,
                    answer.answerId
                )
            }
            true
        }
    }
}
