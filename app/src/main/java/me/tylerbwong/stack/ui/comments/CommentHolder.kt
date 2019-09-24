package me.tylerbwong.stack.ui.comments

import android.view.ViewGroup
import kotlinx.android.synthetic.main.comment_holder.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown

class CommentHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.comment_holder)
) {
    override fun bind(data: Any) {
        (data as? CommentDataModel)?.let { dataModel ->
            commentBody.apply {
                setMarkdown(dataModel.bodyMarkdown)
                setTextIsSelectable(true)
                movementMethod = BetterLinkMovementMethod.getInstance()
            }

            ownerView.bind(dataModel.owner)
        }
    }
}
