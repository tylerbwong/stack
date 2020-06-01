package me.tylerbwong.stack.ui.comments

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Comment
import me.tylerbwong.stack.databinding.CommentHolderBinding
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory

data class CommentItem(internal val comment: Comment, internal val isInCurrentSite: Boolean)

class CommentsAdapter : ListAdapter<CommentItem, CommentHolder>(
    AsyncDifferConfig.Builder(
        object : DiffUtil.ItemCallback<CommentItem>() {
            override fun areItemsTheSame(
                oldItem: CommentItem,
                newItem: CommentItem
            ) = oldItem.comment.commentId == newItem.comment.commentId

            override fun areContentsTheSame(
                oldItem: CommentItem,
                newItem: CommentItem
            ) = oldItem.comment.bodyMarkdown == newItem.comment.bodyMarkdown &&
                    oldItem.comment.owner == newItem.comment.owner &&
                    oldItem.isInCurrentSite == newItem.isInCurrentSite
        }
    ).build()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CommentHolder(parent.inflate(R.layout.comment_holder)).also {
        it.binding.commentBody.setSpannableFactory(noCopySpannableFactory)
    }

    override fun onBindViewHolder(
        holder: CommentHolder,
        position: Int
    ) = holder.bind(getItem(position))
}

class CommentHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    internal val binding = CommentHolderBinding.bind(itemView)

    fun bind(data: CommentItem) {
        binding.commentBody.apply {
            setMarkdown(data.comment.bodyMarkdown)
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }
        binding.ownerView.bind(data.comment.owner, isInCurrentSite = data.isInCurrentSite)
    }
}
