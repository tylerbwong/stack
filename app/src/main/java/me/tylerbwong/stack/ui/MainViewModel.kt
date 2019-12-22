package me.tylerbwong.stack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutError
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutSuccess
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.repository.QuestionRepository
import retrofit2.HttpException

internal class MainViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val repository: QuestionRepository = QuestionRepository(),
    private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val questions: LiveData<List<Question>>
        get() = _questions
    private val _questions = MutableLiveData<List<Question>>()

    internal val profileImage: LiveData<String?>
        get() = _profileImage
    private val _profileImage = MutableLiveData<String?>()

    internal val isAuthenticated: LiveData<Boolean>
        get() = AuthStore.isAuthenticatedLiveData

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

    internal fun fetchUser() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                _profileImage.value = user?.profileImage
            } catch (ex: HttpException) {
                _profileImage.value = null
            }
        }
    }

    internal fun logOut() {
        viewModelScope.launch {
            when (authRepository.logOut()) {
                is LogOutError -> mutableSnackbar.value = Unit
                is LogOutSuccess -> {
                    _profileImage.value = null
                    mutableSnackbar.value = null
                    fetchQuestions()
                }
            }
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
