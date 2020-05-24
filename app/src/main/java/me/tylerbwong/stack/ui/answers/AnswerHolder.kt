package me.tylerbwong.stack.ui.answers

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.answer_holder.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.questions.detail.AnswerItem
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown

class AnswerHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(data: AnswerItem) {
        val answer = data.answer
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
            CommentsBottomSheetDialogFragment.show(
                (it.context as FragmentActivity).supportFragmentManager,
                answer.answerId
            )
            true
        }
    }
}
