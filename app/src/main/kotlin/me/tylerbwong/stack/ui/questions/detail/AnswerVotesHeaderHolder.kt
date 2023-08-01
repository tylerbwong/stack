package me.tylerbwong.stack.ui.questions.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerVotesHeaderHolderBinding
import me.tylerbwong.stack.ui.flag.FlagActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showDialog
import me.tylerbwong.stack.ui.utils.showLogInDialog
import me.tylerbwong.stack.ui.utils.showRegisterOnSiteDialog

class AnswerVotesHeaderHolder(
    parent: ViewGroup
) : DynamicViewBindingHolder<AnswerVotesHeaderItem, AnswerVotesHeaderHolderBinding>(
    parent,
    AnswerVotesHeaderHolderBinding::inflate
) {
    @Suppress("LongMethod")
    override fun AnswerVotesHeaderHolderBinding.bind(item: AnswerVotesHeaderItem) {
        val voteCount = item.upVoteCount - item.downVoteCount
        votes.text = itemView.context.resources.getQuantityString(
            R.plurals.votes,
            voteCount,
            voteCount
        )
        acceptedAnswerCheck.isVisible = item.isAccepted
        val isUpvoted = item.isUpvoted
        val isDownvoted = item.isDownvoted

        upvote.isVisible = isUpvoted != null
        downvote.isVisible = isDownvoted != null
        if (isUpvoted != null && isDownvoted != null) {
            upvote.apply {
                renderSelectedState(
                    selectedColor = R.color.upvoted,
                    isSelected = item.isUpvoted,
                )
                setThrottledOnClickListener {
                    item.handler.toggleAnswerUpvote(
                        answerId = item.id,
                        isSelected = !item.isUpvoted,
                    )
                }
            }
            downvote.apply {
                renderSelectedState(
                    selectedColor = R.color.downvoted,
                    isSelected = item.isDownvoted,
                )
                setThrottledOnClickListener {
                    item.handler.toggleAnswerDownvote(
                        answerId = item.id,
                        isSelected = !item.isDownvoted,
                    )
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
                            setTitle(R.string.hide_answer)
                            setMessage(R.string.hide_answer_message)
                            setPositiveButton(R.string.hide) { _, _ -> item.hideAnswer(item.id) }
                            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                        }
                        true
                    }
                    R.id.flag -> {
                        if (item.isAuthenticated) {
                            if (item.isUserPresent()) {
                                val intent = FlagActivity.makeIntent(
                                    context = itemView.context,
                                    postId = item.id,
                                    postType = 1,
                                )
                                itemView.context.startActivity(intent)
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

    private fun ImageView.renderSelectedState(
        @ColorRes selectedColor: Int,
        isSelected: Boolean,
    ) {
        @ColorInt val color = if (isSelected) {
            ContextCompat.getColor(context, selectedColor)
        } else {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
            typedValue.data
        }
        if (drawable != null) {
            drawable.mutate().colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}
