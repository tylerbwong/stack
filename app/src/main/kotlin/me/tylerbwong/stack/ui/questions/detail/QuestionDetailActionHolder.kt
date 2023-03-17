package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.PostActionHolderBinding
import me.tylerbwong.stack.ui.flag.FlagActivity
import me.tylerbwong.stack.ui.utils.renderSelectedState
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class QuestionDetailActionHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<QuestionActionItem, PostActionHolderBinding>(
    container,
    PostActionHolderBinding::inflate
) {
    override fun PostActionHolderBinding.bind(item: QuestionActionItem) {
        val (handler, question) = item
        upvote.apply {
            renderSelectedState(
                R.color.upvoted,
                question.upVoteCount,
                isSelected = question.isUpVoted
            )
            setThrottledOnClickListener {
                handler.toggleQuestionUpvote(
                    questionId = item.question.questionId,
                    isSelected = !question.isUpVoted,
                )
            }
        }
        bookmark.apply {
            renderSelectedState(
                R.color.favorited,
                question.bookmarkCount,
                isSelected = question.isBookmarked
            )
            setThrottledOnClickListener {
                handler.toggleQuestionFavorite(
                    questionId = item.question.questionId,
                    isSelected = !question.isBookmarked,
                )
            }
        }
        downvote.apply {
            renderSelectedState(
                R.color.downvoted,
                question.downVoteCount,
                isSelected = question.isDownVoted
            )
            setThrottledOnClickListener {
                handler.toggleQuestionDownvote(
                    questionId = item.question.questionId,
                    isSelected = !question.isDownVoted,
                )
            }
        }
        flag.setThrottledOnClickListener {
            val intent = FlagActivity.makeIntent(
                context = itemView.context,
                postId = question.questionId,
                postType = 0,
            )
            itemView.context.startActivity(intent)
        }
    }
}
