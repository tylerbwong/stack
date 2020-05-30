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

class CommentsAdapter : ListAdapter<Comment, CommentHolder>(
    AsyncDifferConfig.Builder(
        object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(
                oldItem: Comment,
                newItem: Comment
            ) = oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(
                oldItem: Comment,
                newItem: Comment
            ) = oldItem.bodyMarkdown == newItem.bodyMarkdown && oldItem.owner == newItem.owner
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

    fun bind(comment: Comment) {
        binding.commentBody.apply {
            setMarkdown(comment.bodyMarkdown)
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }
        binding.ownerView.bind(comment.owner)
    }
}
