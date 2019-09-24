package me.tylerbwong.stack.ui.answers

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.answer_holder.*
import kotlinx.android.synthetic.main.user_view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.GlideApp
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class AnswerHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflateWithoutAttaching(R.layout.answer_holder)
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

            username.text = dataModel.username
            GlideApp.with(itemView)
                .load(dataModel.userImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.user_image_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage)
            userImage.setThrottledOnClickListener { dataModel.onProfilePictureClicked(it.context) }
            reputation.text = dataModel.reputation
            badgeView.badgeCounts = dataModel.badgeCounts

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
