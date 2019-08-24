package me.tylerbwong.stack.ui.questions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.questions.QuestionDataModel
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class SingleTagQuestionsViewModel(
        private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val data: LiveData<List<DynamicDataModel>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicDataModel>>()

    internal fun getQuestionsByTag(tag: String) {
        launchRequest {
            val questions = service.getQuestionsByTags(tags = tag).items
                    .map { QuestionDataModel(it) }
            _data.value = questions
        }
    }
}
