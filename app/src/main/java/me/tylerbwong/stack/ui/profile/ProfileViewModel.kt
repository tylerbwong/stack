package me.tylerbwong.stack.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.answers.AnswerDataModel
import me.tylerbwong.stack.ui.questions.QuestionDataModel

class ProfileViewModel(
        private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal var userId: Int? = null

    internal val questionsData: LiveData<List<QuestionDataModel>>
        get() = _questionsData
    private val _questionsData = MutableLiveData<List<QuestionDataModel>>()

    internal val answersData: LiveData<List<AnswerDataModel>>
        get() = _answersData
    private val _answersData = MutableLiveData<List<AnswerDataModel>>()

    internal fun getUserQuestionsAndAnswers() {
        launchRequest {
            _questionsData.value = service.getUserQuestionsById(userId).items.map {
                QuestionDataModel(it)
            }
            _answersData.value = service.getUserAnswersById(userId).items.map {
                AnswerDataModel(it)
            }
        }
    }
}
