package me.tylerbwong.stack.ui.questions.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.network.service.QuestionService
import javax.inject.Inject

class QuestionDetailMainViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val siteStore: SiteStore,
    private val questionService: QuestionService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionDetailMainViewModel(authRepository, siteStore, questionService) as T
    }
}
