package me.tylerbwong.stack.ui.comments

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.soywiz.klock.seconds
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Comment
import me.tylerbwong.stack.databinding.CommentHolderBinding
import me.tylerbwong.stack.ui.utils.formatElapsedTime
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
            oldItem.comment.owner == newItem.comment.owner &&
            oldItem.comment.creationDate == newItem.comment.creationDate
}

class CommentItem(internal val comment: Comment) : DynamicItem(::CommentHolder)

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
        val (bodyMarkdown, _, creationDate, _, owner, _) = item.comment
        commentBody.setMarkdown(bodyMarkdown)
        commentedDate.apply {
            text = context.getString(
                R.string.commented,
                creationDate.seconds.millisecondsLong.formatElapsedTime(context)
            )
        }
        ownerView.bind(owner)
    }
}
