package me.tylerbwong.stack.ui.questions.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.question_detail_action_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class QuestionDetailActionHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.question_detail_action_holder)
) {
    override fun bind(data: Any) {
        (data as? QuestionDetailActionDataModel)?.let { dataModel ->
            upvote.renderSelectedState(
                    R.color.upvoted,
                    dataModel.upVoteCount,
                    isSelected = dataModel.upvoted
            )
            favorite.renderSelectedState(
                    R.color.favorited,
                    dataModel.favoriteCount,
                    isSelected = dataModel.favorited
            )
            downvote.renderSelectedState(
                    R.color.downvoted,
                    dataModel.downVoteCount,
                    isSelected = dataModel.downvoted
            )

            upvote.setThrottledOnClickListener {
                dataModel.toggleUpvote(isSelected = !dataModel.upvoted)
            }
            favorite.setThrottledOnClickListener {
                dataModel.toggleFavorite(isSelected = !dataModel.favorited)
            }
            downvote.setThrottledOnClickListener {
                dataModel.toggleDownvote(isSelected = !dataModel.downvoted)
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
