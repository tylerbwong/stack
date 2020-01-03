package me.tylerbwong.stack.ui.questions.detail

import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.Question

sealed class QuestionDetailItem
data class QuestionItem(internal val question: Question) : QuestionDetailItem()
data class AnswerHeaderItem(internal val answerCount: Int) : QuestionDetailItem()
data class AnswerItem(internal val answer: Answer) : QuestionDetailItem()

class QuestionDetailItemCallback : DiffUtil.ItemCallback<QuestionDetailItem>() {
    override fun areItemsTheSame(
        oldItem: QuestionDetailItem,
        newItem: QuestionDetailItem
    ) = when {
        oldItem is QuestionItem && newItem is QuestionItem ->
            oldItem.question.questionId == newItem.question.questionId
        oldItem is AnswerHeaderItem && newItem is AnswerHeaderItem -> true
        oldItem is AnswerItem && newItem is AnswerItem ->
            oldItem.answer.answerId == newItem.answer.answerId
        else -> false
    }

    override fun areContentsTheSame(
        oldItem: QuestionDetailItem,
        newItem: QuestionDetailItem
    ) = when {
        oldItem is QuestionItem && newItem is QuestionItem ->
            oldItem.question.title == newItem.question.title &&
                    oldItem.question.bodyMarkdown == newItem.question.bodyMarkdown &&
                    oldItem.question.owner == newItem.question.owner &&
                    oldItem.question.tags == newItem.question.tags
        oldItem is AnswerHeaderItem && newItem is AnswerHeaderItem ->
            oldItem.answerCount == newItem.answerCount
        oldItem is AnswerItem && newItem is AnswerItem ->
            oldItem.answer.isAccepted == newItem.answer.isAccepted &&
                    oldItem.answer.upVoteCount == newItem.answer.upVoteCount &&
                    oldItem.answer.downVoteCount == newItem.answer.downVoteCount &&
                    oldItem.answer.bodyMarkdown == newItem.answer.bodyMarkdown &&
                    oldItem.answer.owner == newItem.answer.owner
        else -> false
    }
}