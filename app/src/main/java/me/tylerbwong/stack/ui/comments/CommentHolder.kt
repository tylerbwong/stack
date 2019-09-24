package me.tylerbwong.stack.ui.comments

import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.comment_holder.*
import kotlinx.android.synthetic.main.user_view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.GlideApp
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class CommentHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflateWithoutAttaching(R.layout.comment_holder)
) {
    override fun bind(data: Any) {
        (data as? CommentDataModel)?.let { dataModel ->
            commentBody.apply {
                setMarkdown(dataModel.bodyMarkdown)
                setTextIsSelectable(true)
                movementMethod = BetterLinkMovementMethod.getInstance()
            }

            username.text = dataModel.username.toHtml()
            GlideApp.with(itemView)
                .load(dataModel.userImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.user_image_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage)
            userImage.setThrottledOnClickListener { dataModel.onProfilePictureClicked(it.context) }
            badgeView.badgeCounts = dataModel.badgeCounts
            reputation.text = dataModel.reputation.toLong().format()
        }
    }
}
