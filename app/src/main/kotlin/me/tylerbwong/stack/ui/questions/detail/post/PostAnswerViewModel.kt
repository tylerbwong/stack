package me.tylerbwong.stack.ui.questions.detail.post

import android.text.TextWatcher
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.entity.AnswerDraftEntity
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostAnswerViewModel @Inject constructor(
    private val service: QuestionService,
    private val draftDao: AnswerDraftDao,
    private val siteStore: SiteStore
) : ViewModel() {
    internal var markdownTextWatcher: TextWatcher? = null
    internal var selectedTabPosition = 0
    internal var questionId = 0
    internal var questionTitle = ""

    val savedDraft: LiveData<String>
        get() = _savedDraft
    private val _savedDraft = MutableLiveData<String>()

    val snackbar: LiveData<PostAnswerState>
        get() = _snackbar
    private val _snackbar = SingleLiveEvent<PostAnswerState>()

    fun postAnswer(markdown: String, isPreview: Boolean = false) {
        _snackbar.value = PostAnswerState.Loading

        viewModelScope.launch {
            try {
                val answer = service.postAnswer(
                    questionId,
                    bodyMarkdown = markdown,
                    preview = isPreview
                ).items

                _snackbar.value = if (answer.isNotEmpty()) {
                    deleteDraft()
                    PostAnswerState.Success
                } else {
                    throw IllegalStateException("Could not post answer")
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                _snackbar.value = PostAnswerState.Error
            }
        }
    }

    fun fetchDraftIfExists() {
        viewModelScope.launch {
            try {
                val draft = draftDao.getAnswerDraft(questionId, siteStore.site) ?: return@launch
                questionTitle = draft.questionTitle
                _savedDraft.value = draft.bodyMarkdown
            } catch (ex: Exception) {
                Timber.w("No draft for questionId $questionId: $ex")
            }
        }
    }

    fun saveDraft(markdown: String) {
        viewModelScope.launch {
            try {
                draftDao.insertAnswerDraft(
                    AnswerDraftEntity(
                        questionId,
                        questionTitle,
                        System.currentTimeMillis(),
                        markdown,
                        siteStore.site
                    )
                )
            } catch (ex: Exception) {
                Timber.e(ex)
                _snackbar.value = PostAnswerState.Error
            }
        }
    }

    fun deleteDraft() {
        viewModelScope.launch {
            try {
                draftDao.deleteDraftById(questionId, siteStore.site)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
}

sealed class PostAnswerState(@StringRes val messageId: Int, @Duration val duration: Int) {
    object Success : PostAnswerState(R.string.post_answer_success, Snackbar.LENGTH_LONG)
    object Loading : PostAnswerState(R.string.posting_answer, Snackbar.LENGTH_INDEFINITE)
    object Error : PostAnswerState(R.string.post_answer_error, Snackbar.LENGTH_LONG)
}
