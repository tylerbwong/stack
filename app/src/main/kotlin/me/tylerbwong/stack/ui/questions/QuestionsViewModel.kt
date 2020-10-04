package me.tylerbwong.stack.ui.questions

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.api.model.CREATION
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.questions.QuestionPage.LINKED
import me.tylerbwong.stack.ui.questions.QuestionPage.RELATED
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS

internal class QuestionsViewModel @ViewModelInject constructor(
    private val service: QuestionService
) : BaseViewModel() {

    internal val data: LiveData<List<Question>>
        get() = _data
    private val _data = MutableLiveData<List<Question>>()

    internal val isMainSortsSupported: Boolean
        get() = page == TAGS

    internal lateinit var page: QuestionPage
    internal var key: String = ""

    @Sort
    private var currentSort: String = CREATION

    internal fun getQuestions(@Sort sort: String = currentSort) {
        when (page) {
            TAGS -> getQuestionsByTag(sort = sort)
            LINKED -> getLinkedQuestions(sort = sort)
            RELATED -> getRelatedQuestions(sort = sort)
        }
    }

    private fun getQuestionsByTag(@Sort sort: String = currentSort) {
        currentSort = sort
        launchRequest {
            _data.value = service.getQuestionsByTags(tags = key, sort = sort).items
        }
    }

    private fun getLinkedQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        val questionId = key.toIntOrNull() ?: return
        launchRequest {
            _data.value = service.getLinkedQuestions(questionId = questionId, sort = sort).items
        }
    }

    private fun getRelatedQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        val questionId = key.toIntOrNull() ?: return
        launchRequest {
            _data.value = service.getRelatedQuestions(questionId = questionId, sort = sort).items
        }
    }
}
