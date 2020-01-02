package me.tylerbwong.stack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.ui.BaseViewModel

internal class HomeViewModel(
    private val repository: QuestionRepository = QuestionRepository(),
    private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val questions: LiveData<List<Question>>
        get() = _questions
    private val _questions = MutableLiveData<List<Question>>()

    @Sort
    internal var currentSort: String = CREATION
    internal var currentQuery: String = ""

    internal fun getQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        launchRequest {
            _questions.value = repository.getQuestions(sort)
        }
    }

    internal fun searchQuestions(query: String = currentQuery) {
        currentQuery = query
        launchRequest {
            _questions.value = service.getQuestionsBySearchString(searchString = query).items
        }
    }

    internal fun fetchQuestions() {
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
