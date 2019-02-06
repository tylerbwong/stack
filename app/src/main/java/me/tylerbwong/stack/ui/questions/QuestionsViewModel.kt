package me.tylerbwong.stack.ui.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.ui.BaseViewModel

internal class QuestionsViewModel(
        private val repository: QuestionRepository = QuestionRepository(),
        private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val questions: LiveData<List<QuestionDataModel>>
        get() = _questions
    private val _questions = MutableLiveData<List<QuestionDataModel>>()

    @Sort
    internal var currentSort: String = CREATION
    internal var currentQuery: String = ""

    internal fun getQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        launchRequest {
            val questionsChannel = withContext(Dispatchers.IO) {
                repository.getQuestions(this, sort)
            }

            for (list in questionsChannel) {
                _questions.value = list.map { QuestionDataModel(it) }
            }
        }
    }

    internal fun searchQuestions(query: String = currentQuery) {
        currentQuery = query
        launchRequest {
            val searchResult = withContext(Dispatchers.IO) {
                service.getQuestionsBySearchString(searchString = query)
            }

            _questions.value = searchResult.await()
                    .items
                    .map { QuestionDataModel(it) }
        }
    }

    internal fun onStart() {
        if (currentQuery.isNotBlank()) {
            searchQuestions()
        } else {
            getQuestions()
        }
    }

    internal fun onQueryTextChange(newText: String?) {
        currentQuery = newText ?: ""

        if (currentQuery.isBlank()) {
            getQuestions()
        }
    }

    internal fun isQueryBlank() = currentQuery.isBlank()
}
