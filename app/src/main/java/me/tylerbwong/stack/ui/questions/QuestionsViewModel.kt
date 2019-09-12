package me.tylerbwong.stack.ui.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.questions.QuestionPage.LINKED
import me.tylerbwong.stack.ui.questions.QuestionPage.RELATED
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.utils.DynamicDataModel

internal class QuestionsViewModel(
    private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val data: LiveData<List<DynamicDataModel>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicDataModel>>()

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
            val questions = service.getQuestionsByTags(tags = key, sort = sort).items
                .map { QuestionDataModel(it) }
            _data.value = questions
        }
    }

    private fun getLinkedQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        val questionId = key.toIntOrNull() ?: return
        launchRequest {
            val questions = service.getLinkedQuestions(questionId = questionId, sort = sort).items
                .map { QuestionDataModel(it) }
            _data.value = questions
        }
    }

    private fun getRelatedQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        val questionId = key.toIntOrNull() ?: return
        launchRequest {
            val questions = service.getRelatedQuestions(questionId = questionId, sort = sort).items
                .map { QuestionDataModel(it) }
            _data.value = questions
        }
    }
}

