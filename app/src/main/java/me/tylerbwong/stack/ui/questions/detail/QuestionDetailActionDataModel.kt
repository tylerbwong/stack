package me.tylerbwong.stack.ui.questions.detail

import me.tylerbwong.stack.ui.utils.DynamicDataModel

class QuestionDetailActionDataModel(
        handler: QuestionDetailActionHandler,
        internal var downvoted: Boolean,
        internal var favorited: Boolean,
        internal var upvoted: Boolean
) : DynamicDataModel(), QuestionDetailActionHandler by handler {

    override fun areItemsThemSame(other: DynamicDataModel) = other is QuestionDetailActionDataModel

    override fun areContentsTheSame(
            other: DynamicDataModel
    ) = other is QuestionDetailActionDataModel && other.downvoted == downvoted
            && other.favorited == favorited && other.upvoted == upvoted

    override fun getViewCreator() = ::QuestionDetailActionHolder
}
