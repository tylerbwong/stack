package me.tylerbwong.stack.ui.comments

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.data.model.Comment
import me.tylerbwong.stack.databinding.CommentHolderBinding
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory

object CommentItemCallback : DiffUtil.ItemCallback<DynamicItem>() {
    override fun areItemsTheSame(
        oldItem: DynamicItem,
        newItem: DynamicItem
    ) = oldItem is CommentItem && newItem is CommentItem &&
            oldItem.comment.commentId == newItem.comment.commentId

    override fun areContentsTheSame(
        oldItem: DynamicItem,
        newItem: DynamicItem
    ) = oldItem is CommentItem && newItem is CommentItem &&
            oldItem.comment.bodyMarkdown == newItem.comment.bodyMarkdown &&
            oldItem.comment.owner == newItem.comment.owner
}

class CommentItem(internal val comment: Comment) : me.tylerbwong.adapter.DynamicItem(::CommentHolder)

class CommentHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<CommentItem, CommentHolderBinding>(
    container,
    CommentHolderBinding::inflate
) {

    init {
        binding.commentBody.setSpannableFactory(noCopySpannableFactory)
    }

    override fun CommentHolderBinding.bind(item: CommentItem) {
        val (bodyMarkdown, _, _, _, owner, _) = item.comment
        commentBody.apply {
            setMarkdown(bodyMarkdown)
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }
        ownerView.bind(owner)
    }
}
