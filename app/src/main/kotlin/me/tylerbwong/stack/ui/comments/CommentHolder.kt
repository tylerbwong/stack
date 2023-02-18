package me.tylerbwong.stack.ui.comments

import android.text.TextWatcher
import android.view.ViewGroup
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
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory

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
                    oldItem.comment.creationDate == newItem.comment.creationDate
        else -> false
    }

}

class CommentItem(internal val comment: Comment) : DynamicItem(::CommentHolder)
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
        val (bodyMarkdown, _, _, creationDate, _, owner, _) = item.comment
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

class AddCommentHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<AddCommentItem, AddCommentHolderBinding>(
    container,
    AddCommentHolderBinding::inflate
) {
    private var textWatcher: TextWatcher? = null

    override fun AddCommentHolderBinding.bind(item: AddCommentItem) {
        addCommentButton.isEnabled = item.initialBody.length >= MIN_COMMENT_LENGTH
        bodyInputLayout.isEnabled = true
        bodyInput.setText(item.initialBody)
        addCommentButton.setOnClickListener {
            item.onSubmitComment(bodyInput.text?.toString() ?: "", BuildConfig.DEBUG)
            addCommentButton.isEnabled = false
            bodyInputLayout.isEnabled = false
        }

        if (textWatcher == null) {
            textWatcher = bodyInput.addTextChangedListener {
                val length = it?.length ?: 0
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
