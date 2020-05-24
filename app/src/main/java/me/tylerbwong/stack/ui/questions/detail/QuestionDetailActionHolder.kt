package me.tylerbwong.stack.ui.questions.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.question_detail_action_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class QuestionDetailActionHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(data: QuestionActionItem) {
        val (handler, question) = data
        upvote.renderSelectedState(
            R.color.upvoted,
            question.upVoteCount,
            isSelected = question.isUpVoted
        )
        favorite.renderSelectedState(
            R.color.favorited,
            question.favoriteCount,
            isSelected = question.isFavorited
        )
        downvote.renderSelectedState(
            R.color.downvoted,
            question.downVoteCount,
            isSelected = question.isDownVoted
        )

        upvote.setThrottledOnClickListener {
            handler.toggleUpvote(isSelected = !question.isUpVoted)
        }
        favorite.setThrottledOnClickListener {
            handler.toggleFavorite(isSelected = !question.isFavorited)
        }
        downvote.setThrottledOnClickListener {
            handler.toggleDownvote(isSelected = !question.isDownVoted)
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
