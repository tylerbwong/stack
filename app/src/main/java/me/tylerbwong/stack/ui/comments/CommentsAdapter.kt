package me.tylerbwong.stack.ui.comments

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.data.model.Comment
import me.tylerbwong.stack.databinding.CommentHolderBinding
import me.tylerbwong.stack.ui.adapter.DelegatedItem
import me.tylerbwong.stack.ui.adapter.ViewBindingViewHolder
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory

object CommentItemCallback : DiffUtil.ItemCallback<DelegatedItem>() {
    override fun areItemsTheSame(
        oldItem: DelegatedItem,
        newItem: DelegatedItem
    ) = oldItem is CommentItem && newItem is CommentItem &&
            oldItem.comment.commentId == newItem.comment.commentId

    override fun areContentsTheSame(
        oldItem: DelegatedItem,
        newItem: DelegatedItem
    ) = oldItem is CommentItem && newItem is CommentItem &&
            oldItem.comment.bodyMarkdown == newItem.comment.bodyMarkdown &&
            oldItem.comment.owner == newItem.comment.owner
}

class CommentItem(internal val comment: Comment) : DelegatedItem(::CommentHolder)

class CommentHolder(
    container: ViewGroup
) : ViewBindingViewHolder<CommentItem, CommentHolderBinding>(
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
