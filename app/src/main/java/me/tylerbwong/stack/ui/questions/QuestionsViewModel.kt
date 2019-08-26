package me.tylerbwong.stack.ui.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.data.auth.AuthProvider
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.StackService
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.ui.BaseViewModel
import retrofit2.HttpException
import timber.log.Timber

internal class QuestionsViewModel(
        private val authRepository: AuthRepository = AuthRepository(),
        private val repository: QuestionRepository = QuestionRepository(),
        private val service: StackService = ServiceProvider.stackService
) : BaseViewModel() {

    internal val questions: LiveData<List<QuestionDataModel>>
        get() = _questions
    private val _questions = MutableLiveData<List<QuestionDataModel>>()

    internal val profileImage: LiveData<String?>
        get() = _profileImage
    private val _profileImage = MutableLiveData<String?>()

    internal val isAuthenticated: LiveData<Boolean>
        get() = AuthProvider.isAuthenticatedLiveData

    @Sort
    internal var currentSort: String = CREATION
    internal var currentQuery: String = ""

    internal fun getQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        launchRequest {
            _questions.value = repository.getQuestions(sort).map { QuestionDataModel(it) }
        }
    }

    internal fun searchQuestions(query: String = currentQuery) {
        currentQuery = query
        launchRequest {
            _questions.value = service.getQuestionsBySearchString(searchString = query)
                    .items
                    .map { QuestionDataModel(it) }
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
                val accountId = AuthProvider.accountId
                if (accountId != null) {
                    val user = authRepository.getCurrentUserNetwork()
                    _profileImage.value = user?.profileImage
                } else {
                    _profileImage.value = null
                }
            } catch (ex: HttpException) {
                Timber.i("User not authenticated")
                _profileImage.value = null
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
