package me.tylerbwong.stack.presentation.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.rx2.await
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.repository.QuestionRepository
import timber.log.Timber
import java.lang.Exception

internal class QuestionsViewModel(
        private val repository: QuestionRepository = QuestionRepository(StackDatabase.getInstance())
) : ViewModel() {

    internal val refreshing: LiveData<Boolean>
        get() = _refreshing
    internal val questions: LiveData<List<Question>>
        get() = _questions

    private val _refreshing = MutableLiveData<Boolean>()
    private val _questions = MutableLiveData<List<Question>>()
    private val questionsJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + questionsJob)

    internal fun getQuestions(@Sort sort: String = CREATION) {
        launchRequest {
            _questions.value = repository.getQuestions(sort)
                    .subscribeOn(Schedulers.io())
                    .awaitLast()
        }
    }

    internal fun searchQuestions(query: String) {
        launchRequest {
            _questions.value = ServiceProvider.questionService
                    .getQuestionsBySearchString(searchString = query)
                    .map { it.items }
                    .subscribeOn(Schedulers.io())
                    .await()
        }
    }

    private fun launchRequest(block: suspend () -> Unit): Job {
        return uiScope.launch {
            try {
                _refreshing.value = true
                block()
            }
            catch (exception: Exception) {
                Timber.e(exception)
            }
            finally {
                _refreshing.value = false
            }
        }
    }

    override fun onCleared() {
        questionsJob.cancel()
    }
}
