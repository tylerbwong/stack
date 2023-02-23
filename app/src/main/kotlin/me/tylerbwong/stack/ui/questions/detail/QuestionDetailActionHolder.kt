package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailActionHolderBinding
import me.tylerbwong.stack.ui.utils.renderSelectedState
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
}
