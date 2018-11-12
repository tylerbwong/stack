package me.tylerbwong.stack.ui.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.rx2.await
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.ui.BaseViewModel

internal class QuestionsViewModel(
        private val repository: QuestionRepository = QuestionRepository(StackDatabase.getInstance()),
        private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val questions: LiveData<List<Question>>
        get() = _questions
    private val _questions = MutableLiveData<List<Question>>()

    internal fun getQuestions(@Sort sort: String = CREATION) {
        launchRequest {
            _questions.value = repository.getQuestions(sort)
                    .subscribeOn(Schedulers.io())
                    .awaitLast()
        }
    }

    internal fun searchQuestions(query: String) {
        launchRequest {
            _questions.value = service
                    .getQuestionsBySearchString(searchString = query)
                    .map { it.items }
                    .subscribeOn(Schedulers.io())
                    .await()
        }
    }
}
