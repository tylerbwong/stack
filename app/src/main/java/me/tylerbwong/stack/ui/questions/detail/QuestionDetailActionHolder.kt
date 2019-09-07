package me.tylerbwong.stack.ui.questions.detail

import android.content.res.ColorStateList
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import kotlinx.android.synthetic.main.question_detail_action_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching

class QuestionDetailActionHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.question_detail_action_holder)
) {
    override fun bind(data: Any) {
        (data as? QuestionDetailActionDataModel)?.let { dataModel ->
            upvote.renderSelectedState(isSelected = dataModel.upvoted)
            upvote.setOnClickListener {
                dataModel.toggleUpvote(isSelected = !dataModel.upvoted)
            }
            favorite.renderSelectedState(isSelected = dataModel.favorited)
            favorite.setOnClickListener {
                dataModel.toggleFavorite(isSelected = !dataModel.favorited)
            }
            downvote.renderSelectedState(isSelected = dataModel.downvoted)
            downvote.setOnClickListener {
                dataModel.toggleDownvote(isSelected = !dataModel.downvoted)
            }
        }
    }

    private fun ImageView.renderSelectedState(isSelected: Boolean) {
        ImageViewCompat.setImageTintList(
                this,
                ColorStateList.valueOf(
                        if (isSelected) {
                            ContextCompat.getColor(context, R.color.colorAccent)
                        } else {
                            ContextCompat.getColor(context, R.color.primaryTextColor)
                        }
                )
        )
    }
}
