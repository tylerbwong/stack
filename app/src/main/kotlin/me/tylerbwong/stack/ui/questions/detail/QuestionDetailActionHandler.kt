package me.tylerbwong.stack.ui.questions.detail

interface QuestionDetailActionHandler {
    fun toggleDownvote(isSelected: Boolean)
    fun toggleFavorite(isSelected: Boolean)
    fun toggleUpvote(isSelected: Boolean)
}
