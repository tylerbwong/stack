package me.tylerbwong.stack.ui.comments

import android.text.TextWatcher
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import com.soywiz.klock.seconds
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Comment
import me.tylerbwong.stack.databinding.AddCommentHolderBinding
import me.tylerbwong.stack.databinding.CommentHolderBinding
import me.tylerbwong.stack.ui.flag.FlagActivity
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.renderSelectedState
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showDialog
import me.tylerbwong.stack.ui.utils.showLogInDialog

object CommentItemCallback : DiffUtil.ItemCallback<DynamicItem>() {
    override fun areItemsTheSame(
        oldItem: DynamicItem,
        newItem: DynamicItem
    ) = oldItem is CommentItem && newItem is CommentItem &&
            oldItem.comment.commentId == newItem.comment.commentId &&
            oldItem.comment.postId == newItem.comment.postId ||
            oldItem is AddCommentItem && newItem is AddCommentItem

    override fun areContentsTheSame(
        oldItem: DynamicItem,
        newItem: DynamicItem
    ) = when {
        oldItem is CommentItem && newItem is CommentItem ->
            oldItem.comment.bodyMarkdown == newItem.comment.bodyMarkdown &&
                    oldItem.comment.owner == newItem.comment.owner &&
                    oldItem.comment.creationDate == newItem.comment.creationDate &&
                    oldItem.comment.upvoted == newItem.comment.upvoted &&
                    oldItem.comment.score == newItem.comment.score
        else -> false
    }

}

class CommentItem(
    internal val comment: Comment,
    internal val hideComment: (Int) -> Unit,
    internal val upvoteToggle: (Int, Boolean) -> Unit,
) : DynamicItem(::CommentHolder)

class AddCommentItem(
    internal val initialBody: String,
    internal val onSubmitComment: (body: String, isPreview: Boolean) -> Unit,
) : DynamicItem(::AddCommentHolder)

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
        val (bodyMarkdown, commentId, _, creationDate, _, owner, _) = item.comment
        commentBody.setMarkdown(bodyMarkdown)
        commentedDate.apply {
            text = context.getString(
                R.string.commented,
                creationDate.seconds.millisecondsLong.formatElapsedTime(context)
            )
        }
        ownerView.bind(owner)
        val score = item.comment.score
        val upvoted = item.comment.upvoted
        val showAuthContent = score != null && upvoted != null
        upvote.isVisible = showAuthContent
        if (score != null && upvoted != null) {
            upvote.apply {
                renderSelectedState(
                    R.color.upvoted,
                    score,
                    isSelected = upvoted,
                )
                setThrottledOnClickListener {
                    if (commentId != null) {
                        item.upvoteToggle(commentId, !upvoted)
                    }
                }
            }
        }
        moreOptions.setThrottledOnClickListener {
            val popup = PopupMenu(it.context, it)
            popup.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.hide -> {
                        it.context.showDialog {
                            setIcon(R.drawable.ic_baseline_visibility_off)
                            setTitle(R.string.hide_comment)
                            setMessage(R.string.hide_comment_message)
                            setPositiveButton(R.string.hide) { _, _ ->
                                if (commentId != null) {
                                    item.hideComment(commentId)
                                }
                            }
                            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                        }
                        true
                    }
                    R.id.flag -> {
                        if (showAuthContent) {
                            if (commentId != null) {
                                val intent = FlagActivity.makeIntent(
                                    context = itemView.context,
                                    postId = commentId,
                                    postType = 2,
                                )
                                itemView.context.startActivity(intent)
                            }
                        } else {
                            itemView.context.showLogInDialog(alternateLogInMessage = R.string.log_in_message_flag)
                        }
                        true
                    }
                    else -> false
                }
            }
            popup.menuInflater.inflate(R.menu.menu_content_filter, popup.menu)
            popup.show()
        }
    }
}

class AddCommentHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AddCommentItem, AddCommentHolderBinding>(
    container,
    AddCommentHolderBinding::inflate
) {
    private var textWatcher: TextWatcher? = null

    override fun AddCommentHolderBinding.bind(item: AddCommentItem) {
        addCommentButton.isEnabled = item.initialBody.trim().length >= MIN_COMMENT_LENGTH
        bodyInputLayout.isEnabled = true
        bodyInput.setText(item.initialBody)
        addCommentButton.setOnClickListener {
            item.onSubmitComment(bodyInput.text?.toString() ?: "", BuildConfig.DEBUG)
            addCommentButton.isEnabled = false
            bodyInputLayout.isEnabled = false
        }

        if (textWatcher == null) {
            textWatcher = bodyInput.addTextChangedListener {
                val length = it?.trim()?.length ?: 0
                addCommentButton.isEnabled = length >= MIN_COMMENT_LENGTH
            }
        } else {
            bodyInput.addTextChangedListener(textWatcher)
        }
    }

    companion object {
        private const val MIN_COMMENT_LENGTH = 15
    }
}
