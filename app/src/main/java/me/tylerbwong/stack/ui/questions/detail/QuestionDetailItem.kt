package me.tylerbwong.stack.ui.questions.detail

import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.ViewHolderProvider
import me.tylerbwong.stack.ui.answers.AnswerHolder

sealed class QuestionDetailItem(viewHolderProvider: ViewHolderProvider) : DynamicItem(viewHolderProvider)
data class QuestionMainItem(internal val question: Question) : QuestionDetailItem(::QuestionDetailHolder)
data class QuestionActionItem(
    internal val handler: QuestionDetailActionHandler,
    internal val question: Question
) : QuestionDetailItem(::QuestionDetailActionHolder)
data class AnswerHeaderItem(internal val answerCount: Int) : QuestionDetailItem(::AnswerHeaderViewHolder)
data class AnswerItem(internal val answer: Answer) : QuestionDetailItem(::AnswerHolder)

object QuestionDetailItemCallback : DiffUtil.ItemCallback<DynamicItem>() {
    override fun areItemsTheSame(oldItem: DynamicItem, newItem: DynamicItem) = when {
        oldItem is QuestionMainItem && newItem is QuestionMainItem ->
            oldItem.question.questionId == newItem.question.questionId
        oldItem is QuestionActionItem && newItem is QuestionActionItem -> true
        oldItem is AnswerHeaderItem && newItem is AnswerHeaderItem -> true
        oldItem is AnswerItem && newItem is AnswerItem ->
            oldItem.answer.answerId == newItem.answer.answerId
        else -> false
    }

    @Suppress("ComplexMethod") // TODO Delegate to each item
    override fun areContentsTheSame(oldItem: DynamicItem, newItem: DynamicItem) = when {
        oldItem is QuestionMainItem && newItem is QuestionMainItem ->
            oldItem.question.title == newItem.question.title &&
                    oldItem.question.bodyMarkdown == newItem.question.bodyMarkdown &&
                    oldItem.question.owner == newItem.question.owner &&
                    oldItem.question.tags == newItem.question.tags &&
                    oldItem.question.creationDate == newItem.question.creationDate
        oldItem is QuestionActionItem && newItem is QuestionActionItem ->
            oldItem.question.isDownVoted == newItem.question.isDownVoted &&
                    oldItem.question.isBookmarked == newItem.question.isBookmarked &&
                    oldItem.question.isUpVoted == newItem.question.isUpVoted &&
                    oldItem.question.downVoteCount == newItem.question.downVoteCount &&
                    oldItem.question.bookmarkCount == newItem.question.bookmarkCount &&
                    oldItem.question.upVoteCount == newItem.question.upVoteCount
        oldItem is AnswerHeaderItem && newItem is AnswerHeaderItem ->
            oldItem.answerCount == newItem.answerCount
        oldItem is AnswerItem && newItem is AnswerItem ->
            oldItem.answer.isAccepted == newItem.answer.isAccepted &&
                    oldItem.answer.upVoteCount == newItem.answer.upVoteCount &&
                    oldItem.answer.downVoteCount == newItem.answer.downVoteCount &&
                    oldItem.answer.bodyMarkdown == newItem.answer.bodyMarkdown &&
                    oldItem.answer.owner == newItem.answer.owner &&
                    oldItem.answer.creationDate == newItem.answer.creationDate
        else -> false
    }
}
