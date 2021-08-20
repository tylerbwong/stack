package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import androidx.core.view.isVisible
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.AnswerVotesHeaderHolderBinding

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
    }
}
