package me.tylerbwong.stack.ui.questions.detail

import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class QuestionDetailActionDataModel(
        handler: QuestionDetailActionHandler,
        question: Question
) : DynamicDataModel(), QuestionDetailActionHandler by handler {

    internal val downVoteCount = question.downVoteCount
    internal var downvoted = question.downvoted
    internal val favoriteCount = question.favoriteCount
    internal var favorited = question.favorited
    internal val upVoteCount = question.upVoteCount
    internal var upvoted = question.upvoted

    override fun areItemsThemSame(other: DynamicDataModel) = other is QuestionDetailActionDataModel

    override fun areContentsTheSame(
            other: DynamicDataModel
    ) = other is QuestionDetailActionDataModel && other.downVoteCount == downVoteCount &&
            other.downvoted == downvoted && other.favoriteCount == favoriteCount &&
            other.favorited == favorited && other.upVoteCount == upVoteCount &&
            other.upvoted == upvoted

    override fun getViewCreator() = ::QuestionDetailActionHolder
}
