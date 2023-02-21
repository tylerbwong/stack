package me.tylerbwong.stack.ui.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.CREATION
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.questions.QuestionPage.LINKED
import me.tylerbwong.stack.ui.questions.QuestionPage.RELATED
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import javax.inject.Inject

@HiltViewModel
internal class QuestionsViewModel @Inject constructor(
    private val service: QuestionService
) : BaseViewModel() {

    internal val data: LiveData<List<QuestionItem>>
        get() = _data
    private val _data = MutableLiveData<List<QuestionItem>>()

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
        getQuestions(sort = sort) {
            service.getQuestionsByTags(sort = sort, tags = key).items
        }
    }

    private fun getLinkedQuestions(@Sort sort: String = currentSort) {
        getQuestions(sort = sort) {
            key.toIntOrNull()?.let { questionId ->
                service.getLinkedQuestions(questionId = questionId, sort = sort).items
            }
        }
    }

    private fun getRelatedQuestions(@Sort sort: String = currentSort) {
        getQuestions(sort = sort) {
            key.toIntOrNull()?.let { questionId ->
                service.getRelatedQuestions(questionId = questionId, sort = sort).items
            }
        }
    }

    private fun getQuestions(
        @Sort sort: String = currentSort,
        questionsProvider: suspend () -> List<Question>?
    ) {
        currentSort = sort
        launchRequest {
            val questions = questionsProvider()
            if (questions != null) {
                _data.value = questions.map { QuestionItem(it) }
            }
        }
    }
}
