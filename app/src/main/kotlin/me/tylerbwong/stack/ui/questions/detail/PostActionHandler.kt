package me.tylerbwong.stack.ui.questions.detail

interface PostActionHandler {
    fun toggleQuestionDownvote(questionId: Int, isSelected: Boolean)
    fun toggleQuestionFavorite(questionId: Int, isSelected: Boolean)
    fun toggleQusetionUpvote(questionId: Int, isSelected: Boolean)

    fun toggleAnswerDownvote(answerId: Int, isSelected: Boolean)
    fun toggleAnswerUpvote(answerId: Int, isSelected: Boolean)
}
