package me.tylerbwong.stack.ui.answers

import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class AnswerDataModel(answer: Answer) : DynamicDataModel() {

    internal val answerId = answer.answerId
    internal val isAccepted = answer.isAccepted
    internal val voteCount = answer.upVoteCount - answer.downVoteCount
    internal val answerBody = answer.bodyMarkdown
    internal val owner = answer.owner
    internal val lastEditorName = answer.lastEditor?.displayName

    override fun areItemsThemSame(
        other: DynamicDataModel
    ) = other is AnswerDataModel && other.answerId == answerId

    override fun areContentsTheSame(
        other: DynamicDataModel
    ) = other is AnswerDataModel && other.isAccepted == isAccepted &&
            other.voteCount == voteCount && other.answerBody == answerBody && other.owner == owner

    override fun getViewCreator() = ::AnswerHolder
}
