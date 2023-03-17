package me.tylerbwong.stack.ui.questions.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerVotesHeaderHolderBinding
import me.tylerbwong.stack.ui.flag.FlagActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class AnswerVotesHeaderHolder(
    parent: ViewGroup
) : DynamicViewBindingHolder<AnswerVotesHeaderItem, AnswerVotesHeaderHolderBinding>(
    parent,
    AnswerVotesHeaderHolderBinding::inflate
) {
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
        // Presence of these indicates auth
        val showFlag = item.isUpvoted != null && item.isDownvoted != null

        flag.isVisible = showFlag
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
            flag.setThrottledOnClickListener {
                val intent = FlagActivity.makeIntent(
                    context = itemView.context,
                    postId = item.id,
                    postType = 1,
                )
                itemView.context.startActivity(intent)
            }
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
