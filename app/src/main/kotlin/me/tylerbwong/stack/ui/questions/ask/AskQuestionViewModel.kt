package me.tylerbwong.stack.ui.questions.ask

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

    val askQuestionState: LiveData<AskQuestionState>
        get() = _askQuestionState
    private val _askQuestionState = SingleLiveEvent<AskQuestionState>()

    val questionDraft: LiveData<QuestionDraft>
        get() = _questionDraft
    private val _questionDraft = MutableLiveData<QuestionDraft>()

    fun askQuestion(title: String, body: String, tags: String, isPreview: Boolean) {
        viewModelScope.launch {
            try {
                val response = questionService.addQuestion(
                    title,
                    body,
                    tags.replace(",", ";"),
                    preview = isPreview
                )
                _askQuestionState.value = if (isPreview) {
                    AskQuestionSuccessPreview
                } else {
                    val question = response.items.firstOrNull()
                    if (question != null) {
                        AskQuestionSuccess(question.questionId)
                    } else {
                        AskQuestionError()
                    }
                }
            } catch (ex: Exception) {
                _askQuestionState.value = AskQuestionError(
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

sealed class AskQuestionState
data class AskQuestionSuccess(val questionId: Int) : AskQuestionState()
object AskQuestionSuccessPreview : AskQuestionState()
data class AskQuestionError(val errorMessage: String? = null) : AskQuestionState()
