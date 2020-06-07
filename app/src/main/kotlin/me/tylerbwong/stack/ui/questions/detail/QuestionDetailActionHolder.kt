package me.tylerbwong.stack.ui.questions.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailActionHolderBinding
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class QuestionDetailActionHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<QuestionActionItem, QuestionDetailActionHolderBinding>(
    container,
    QuestionDetailActionHolderBinding::inflate
) {
    override fun QuestionDetailActionHolderBinding.bind(item: QuestionActionItem) {
        val (handler, question) = item
        upvote.apply {
            renderSelectedState(
                R.color.upvoted,
                question.upVoteCount,
                isSelected = question.isUpVoted
            )
            setThrottledOnClickListener {
                handler.toggleUpvote(isSelected = !question.isUpVoted)
            }
        }
        bookmark.apply {
            renderSelectedState(
                R.color.favorited,
                question.bookmarkCount,
                isSelected = question.isBookmarked
            )
            setThrottledOnClickListener {
                handler.toggleFavorite(isSelected = !question.isBookmarked)
            }
        }
        downvote.apply {
            renderSelectedState(
                R.color.downvoted,
                question.downVoteCount,
                isSelected = question.isDownVoted
            )
            setThrottledOnClickListener {
                handler.toggleDownvote(isSelected = !question.isDownVoted)
            }
        }
    }

    private fun TextView.renderSelectedState(
        @ColorRes selectedColor: Int,
        value: Int,
        isSelected: Boolean
    ) {
        @ColorInt val color = if (isSelected) {
            ContextCompat.getColor(context, selectedColor)
        } else {
            ContextCompat.getColor(context, R.color.primaryTextColor)
        }
        setTextColor(color)
        text = value.toLong().format()
        compoundDrawables.forEach {
            if (it != null) {
                it.mutate().colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }
}
