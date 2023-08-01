package me.tylerbwong.stack.ui.comments

import android.content.Context
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.textfield.TextInputLayout
import com.soywiz.klock.seconds
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Comment
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.databinding.AddCommentHolderBinding
import me.tylerbwong.stack.databinding.CommentHolderBinding
import me.tylerbwong.stack.ui.flag.FlagActivity
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.renderSelectedState
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showDialog
import me.tylerbwong.stack.ui.utils.showLogInDialog
import me.tylerbwong.stack.ui.utils.showRegisterOnSiteDialog

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
    internal val isAuthenticated: Boolean,
    internal val site: Site?,
    internal val siteJoinUrl: (Site) -> String,
    internal val isUserPresent: () -> Boolean,
    internal val hideComment: (Int) -> Unit,
    internal val upvoteToggle: (Int, Boolean) -> Unit,
) : DynamicItem(::CommentHolder)

class AddCommentItem(
    internal val isAuthenticated: Boolean,
    internal val site: Site?,
    internal val siteJoinUrl: (Site) -> String,
    internal val isUserPresent: () -> Boolean,
    internal val getBody: () -> String,
    internal val setBody: (String) -> Unit,
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

    @Suppress("ComplexMethod", "LongMethod")
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
                        if (item.isAuthenticated) {
                            if (item.isUserPresent()) {
                                if (commentId != null) {
                                    val intent = FlagActivity.makeIntent(
                                        context = itemView.context,
                                        postId = commentId,
                                        postType = 2,
                                    )
                                    itemView.context.startActivity(intent)
                                }
                            } else if (item.site != null) {
                                itemView.context.showRegisterOnSiteDialog(
                                    site = item.site,
                                    siteUrl = item.siteJoinUrl(item.site),
                                    titleResId = R.string.register_on_site_contribute,
                                )
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
        bodyInputLayout.hint = itemView.context.getString(
            R.string.add_comment_min_character,
            MIN_COMMENT_LENGTH,
        )
        val bodyLength = item.getBody().length
        addCommentButton.isEnabled = bodyLength in MIN_COMMENT_LENGTH .. MAX_COMMENT_LENGTH
        bodyInputLayout.isEnabled = true
        bodyInput.setText(item.getBody())
        setInputLayoutStatus(itemView.context, bodyInputLayout, bodyLength)
        addCommentButton.setOnClickListener {
            if (item.isAuthenticated) {
                if (item.isUserPresent()) {
                    item.onSubmitComment(bodyInput.text?.toString() ?: "", BuildConfig.DEBUG)
                    addCommentButton.isEnabled = false
                    bodyInputLayout.isEnabled = false
                } else if (item.site != null) {
                    itemView.context.showRegisterOnSiteDialog(
                        site = item.site,
                        siteUrl = item.siteJoinUrl(item.site),
                        titleResId = R.string.register_on_site_contribute,
                    )
                }
            } else {
                itemView.context.showLogInDialog(alternateLogInMessage = R.string.log_in_message_flag)
            }
        }
        if (textWatcher == null) {
            textWatcher = bodyInput.addTextChangedListener {
                val text = it
                val length = text?.length ?: 0
                addCommentButton.isEnabled = length in MIN_COMMENT_LENGTH .. MAX_COMMENT_LENGTH
                setInputLayoutStatus(itemView.context, bodyInputLayout, length)
                if (text != null) {
                    item.setBody(text.toString())
                }
            }
        } else {
            bodyInput.addTextChangedListener(textWatcher)
        }
    }

    private fun setInputLayoutStatus(context: Context, layout: TextInputLayout, length: Int) {
        when {
            length in 1 until MIN_COMMENT_LENGTH -> {
                layout.error = null
                layout.hint = context.getString(
                    R.string.add_comment_min_character_progress,
                    MIN_COMMENT_LENGTH - length,
                )
            }
            length in MIN_COMMENT_LENGTH .. MAX_COMMENT_LENGTH -> {
                val remaining = MAX_COMMENT_LENGTH - length
                layout.error = null
                layout.hint = context.resources.getQuantityString(
                    R.plurals.add_comment_max_character,
                    remaining,
                    remaining,
                )
            }
            length > MAX_COMMENT_LENGTH -> {
                val overflow = length - MAX_COMMENT_LENGTH
                layout.hint = context.getString(R.string.add_comment)
                layout.error = context.resources.getQuantityString(
                    R.plurals.add_comment_too_many_characters,
                    overflow,
                    overflow,
                )
            }
            else -> {
                layout.error = null
                layout.hint = context.getString(
                    R.string.add_comment_min_character,
                    MIN_COMMENT_LENGTH,
                )
            }
        }
    }

    companion object {
        private const val MIN_COMMENT_LENGTH = 15
        private const val MAX_COMMENT_LENGTH = 600
    }
}
