package me.tylerbwong.stack.ui.questions.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.service.QuestionService
import javax.inject.Inject

class QuestionDetailMainViewModelFactory @Inject constructor(
    private val authStore: AuthStore,
    private val questionService: QuestionService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionDetailMainViewModel(authStore, questionService) as T
    }
}
