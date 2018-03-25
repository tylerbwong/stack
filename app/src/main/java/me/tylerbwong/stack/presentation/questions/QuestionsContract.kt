package me.tylerbwong.stack.presentation.questions

import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.network.service.QuestionService.Companion.CREATION
import me.tylerbwong.stack.presentation.BasePresenter
import me.tylerbwong.stack.presentation.BaseView

class QuestionsContract {
    interface View : BaseView {
        fun setQuestions(questions: List<Question>)

        fun setRefreshing(isRefreshing: Boolean)
    }

    interface Presenter : BasePresenter {
        fun getQuestions(@QuestionService.Companion.Sort sort: String = CREATION)

        fun searchQuestions(query: String)
    }
}
