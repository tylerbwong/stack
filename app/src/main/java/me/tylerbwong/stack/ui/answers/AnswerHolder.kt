package me.tylerbwong.stack.ui.answers

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerHolderBinding
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.questions.detail.AnswerItem
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown

class AnswerHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    internal val binding = AnswerHolderBinding.bind(itemView)

    fun bind(data: AnswerItem) {
        val answer = data.answer
        val voteCount = answer.upVoteCount - answer.downVoteCount
        binding.votes.text =
            itemView.context.resources.getQuantityString(R.plurals.votes, voteCount, voteCount)
        binding.acceptedAnswerCheck.isVisible = answer.isAccepted

        binding.answerBody.apply {
            setMarkdown(answer.bodyMarkdown)
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }

        binding.lastEditor.apply {
            isVisible = answer.lastEditor != null
            text = context.getString(R.string.last_edited_by, answer.lastEditor?.displayName)
        }

        binding.ownerView.bind(answer.owner)

        itemView.setOnLongClickListener {
            CommentsBottomSheetDialogFragment.show(
                (it.context as FragmentActivity).supportFragmentManager,
                answer.answerId
            )
            true
        }
    }
}
