package me.tylerbwong.stack.ui.questions.detail.post

import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.AnswerRequest
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.StackService
import timber.log.Timber

class PostAnswerViewModel(
        private val service: StackService = ServiceProvider.stackService
) : ViewModel() {
    internal var markdownTextWatcher: TextWatcher? = null
    internal var selectedTabPosition = 0
    internal var questionId = 0

    val snackbar: LiveData<Int?>
        get() = _snackbar
    private val _snackbar = MutableLiveData<Int?>()

    fun postAnswer(markdown: String) {
        _snackbar.value = R.string.posting_answer

        viewModelScope.launch {
            try {
                val answer = service.postAnswer(questionId, answerRequest = AnswerRequest.from(markdown)).items

                _snackbar.value = if (answer.isNotEmpty()) {
                    R.string.post_answer_success
                } else {
                    R.string.post_answer_error
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                _snackbar.value = R.string.post_answer_error
            }
        }
    }
}
