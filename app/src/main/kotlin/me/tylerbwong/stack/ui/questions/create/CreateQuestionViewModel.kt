package me.tylerbwong.stack.ui.questions.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.api.utils.toErrorResponse
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.data.toQuestionDraft
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    private val questionService: QuestionService,
    private val questionDraftDao: QuestionDraftDao,
    private val siteStore: SiteStore
) : BaseViewModel() {

    private var id: Int = -1

    val createQuestionState: LiveData<CreateQuestionState>
        get() = _createQuestionState
    private val _createQuestionState = SingleLiveEvent<CreateQuestionState>()

    val questionDraft: LiveData<QuestionDraft>
        get() = _questionDraft
    private val _questionDraft = MutableLiveData<QuestionDraft>()

    fun createQuestion(title: String, body: String, tags: String, isPreview: Boolean) {
        viewModelScope.launch {
            try {
                val response = questionService.addQuestion(
                    title,
                    body,
                    tags.replace(",", ";"),
                    preview = isPreview
                )
                _createQuestionState.value = if (isPreview) {
                    CreateQuestionSuccessPreview
                } else {
                    val question = response.items.firstOrNull()
                    if (question != null) {
                        CreateQuestionSuccess(question.questionId)
                    } else {
                        CreateQuestionError()
                    }
                }
            } catch (ex: Exception) {
                _createQuestionState.value = CreateQuestionError(
                    (ex as? HttpException)?.toErrorResponse()?.errorMessage
                )
            }
        }
    }

    fun saveDraft(title: String, body: String, tags: String, timestampProvider: (Long) -> String) {
        launchRequest {
            val id = questionDraftDao.insertQuestionDraft(
                QuestionDraftEntity(title, System.currentTimeMillis(), body, tags, siteStore.site)
            )
            fetchDraft(id.toInt(), timestampProvider)
        }
    }

    fun deleteDraft(id: Int) {
        launchRequest {
            questionDraftDao.deleteDraftById(id, siteStore.site)
        }
    }

    fun fetchDraft(id: Int, timestampProvider: (Long) -> String) {
        if (id != -1) {
            this.id = id
            launchRequest {
                _questionDraft.value = questionDraftDao.getQuestionDraft(id, siteStore.site)
                    .toQuestionDraft(timestampProvider)
            }
        }
    }
}

sealed class CreateQuestionState
data class CreateQuestionSuccess(val questionId: Int) : CreateQuestionState()
object CreateQuestionSuccessPreview : CreateQuestionState()
data class CreateQuestionError(val errorMessage: String? = null) : CreateQuestionState()
