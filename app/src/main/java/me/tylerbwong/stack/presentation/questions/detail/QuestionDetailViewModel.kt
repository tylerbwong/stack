package me.tylerbwong.stack.presentation.questions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.await
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.presentation.BaseViewModel

class QuestionDetailViewModel(
        private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val question: LiveData<Question>
        get() = _question
    private val _question = MutableLiveData<Question>()

    internal val answers: LiveData<List<Answer>>
        get() = _answers
    private val _answers = MutableLiveData<List<Answer>>()

    internal var questionId: Int = 0

    internal fun getQuestionDetails() {
        launchRequest {
            val response = Single.zip(
                    service.getQuestionDetails(questionId)
                            .map { it.items.first() }
                            .subscribeOn(Schedulers.io()),
                    service.getQuestionAnswers(questionId)
                            .map { it.items.sortedBy { answer -> !answer.isAccepted } }
                            .subscribeOn(Schedulers.io()),
                    BiFunction { question: Question, answers: List<Answer> -> question to answers }
            ).subscribeOn(Schedulers.io()).await()

            _question.value = response.first
            _answers.value = response.second
        }
    }
}
