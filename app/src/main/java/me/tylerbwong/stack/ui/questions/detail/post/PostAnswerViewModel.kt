package me.tylerbwong.stack.ui.questions.detail.post

import android.text.TextWatcher
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.StackService
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import timber.log.Timber

class PostAnswerViewModel(
        private val service: StackService = ServiceProvider.stackService
) : ViewModel() {
    internal var markdownTextWatcher: TextWatcher? = null
    internal var selectedTabPosition = 0
    internal var questionId = 0

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
}

sealed class PostAnswerState(@StringRes val messageId: Int, @Duration val duration: Int) {
    object Success : PostAnswerState(R.string.post_answer_success, Snackbar.LENGTH_LONG)
    object Loading : PostAnswerState(R.string.posting_answer, Snackbar.LENGTH_INDEFINITE)
    object Error : PostAnswerState(R.string.post_answer_error, Snackbar.LENGTH_LONG)
}
