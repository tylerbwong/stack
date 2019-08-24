package me.tylerbwong.stack.ui.questions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Sort
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

    internal var currentTag: String = ""
    @Sort
    internal var currentSort: String = CREATION

    internal fun getQuestionsByTag(tag: String = currentTag, @Sort sort: String = currentSort) {
        currentTag = tag
        currentSort = sort
        launchRequest {
            val questions = service.getQuestionsByTags(tags = tag, sort = sort).items
                    .map { QuestionDataModel(it) }
            _data.value = questions
        }
    }
}
