package me.tylerbwong.stack.ui.questions.detail

import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class QuestionDetailDataModel(internal val question: Question) : DynamicDataModel() {

    internal val questionTitle = question.title
    internal val answerCount = question.answerCount
    internal val questionBody = question.bodyMarkdown
    internal val questionId = question.questionId
    internal val owner = question.owner
    internal val lastEditorName = question.lastEditor?.displayName
    internal val tags = question.tags

    override fun areItemsThemSame(
        other: DynamicDataModel
    ) = other is QuestionDetailDataModel && other.questionId == questionId

    override fun areContentsTheSame(
        other: DynamicDataModel
    ) = other is QuestionDetailDataModel && other.questionTitle == questionTitle &&
            other.answerCount == answerCount && other.questionBody == questionBody &&
            other.owner == owner

    override fun getViewCreator() = ::QuestionDetailHolder
}
