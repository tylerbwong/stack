package me.tylerbwong.stack.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.answers.AnswerDataModel
import me.tylerbwong.stack.ui.questions.QuestionDataModel
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class ProfileViewModel(
        private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal var userId: Int? = null

    internal val questionsData: LiveData<List<DynamicDataModel>>
        get() = _questionsData
    private val _questionsData = MutableLiveData<List<DynamicDataModel>>()

    internal val answersData: LiveData<List<DynamicDataModel>>
        get() = _answersData
    private val _answersData = MutableLiveData<List<DynamicDataModel>>()

    internal fun getUserQuestionsAndAnswers() {
        launchRequest {
            val questionsRequest = withContext(Dispatchers.IO) {
                service.getUserQuestionsById(userId)
            }
            val answersRequest = withContext(Dispatchers.IO) {
                service.getUserAnswersById(userId)
            }

            _questionsData.value = questionsRequest.await().items.map { QuestionDataModel(it) }
            _answersData.value = answersRequest.await().items.map { AnswerDataModel(it) }
        }
    }
}
