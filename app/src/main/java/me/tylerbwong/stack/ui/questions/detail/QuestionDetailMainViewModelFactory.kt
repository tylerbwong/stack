package me.tylerbwong.stack.ui.questions.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.network.service.QuestionService

class QuestionDetailMainViewModelFactory(
    private val authRepository: AuthRepository,
    private val questionService: QuestionService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionDetailMainViewModel(authRepository, questionService) as T
    }
}
