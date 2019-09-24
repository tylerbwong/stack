package me.tylerbwong.stack.ui.answers

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.answer_holder.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown

class AnswerHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.answer_holder)
) {
    override fun bind(data: Any) {
        (data as? AnswerDataModel)?.let { dataModel ->
            val voteCount = dataModel.voteCount
            votes.text =
                itemView.context.resources.getQuantityString(R.plurals.votes, voteCount, voteCount)
            acceptedAnswerCheck.isVisible = dataModel.isAccepted

            answerBody.apply {
                setMarkdown(dataModel.answerBody)
                setTextIsSelectable(true)
                movementMethod = BetterLinkMovementMethod.getInstance()
            }

            lastEditor.apply {
                isVisible = dataModel.lastEditorName != null
                text = context.getString(R.string.last_edited_by, dataModel.lastEditorName)
            }

            ownerView.bind(dataModel.owner)

            itemView.setOnLongClickListener {
                CommentsBottomSheetDialogFragment.show(
                    (it.context as FragmentActivity).supportFragmentManager,
                    dataModel.answerId
                )
                true
            }
        }
    }
}
