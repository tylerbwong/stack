package me.tylerbwong.stack.ui.questions.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailActionHolderBinding
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class QuestionDetailActionHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = QuestionDetailActionHolderBinding.bind(itemView)

    fun bind(data: QuestionActionItem) {
        val (handler, question) = data
        binding.upvote.apply {
            renderSelectedState(
                R.color.upvoted,
                question.upVoteCount,
                isSelected = question.isUpVoted
            )
            setThrottledOnClickListener {
                handler.toggleUpvote(isSelected = !question.isUpVoted)
            }
        }
        binding.bookmark.apply {
            renderSelectedState(
                R.color.favorited,
                question.bookmarkCount,
                isSelected = question.isBookmarked
            )
            setThrottledOnClickListener {
                handler.toggleFavorite(isSelected = !question.isBookmarked)
            }
        }
        binding.downvote.apply {
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
