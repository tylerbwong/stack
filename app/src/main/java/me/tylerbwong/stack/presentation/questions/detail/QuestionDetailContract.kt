package me.tylerbwong.stack.presentation.questions.detail

import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.presentation.BasePresenter
import me.tylerbwong.stack.presentation.BaseView

class QuestionDetailContract {
    interface View : BaseView {
        fun setQuestion(question: Question)

        fun setAnswers(answers: List<Answer>)

        fun setRefreshing(isRefreshing: Boolean)
    }

    interface Presenter : BasePresenter
}
