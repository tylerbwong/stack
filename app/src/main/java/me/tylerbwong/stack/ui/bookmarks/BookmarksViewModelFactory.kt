package me.tylerbwong.stack.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.repository.SiteRepository
import javax.inject.Inject

class BookmarksViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val siteRepository: SiteRepository,
    private val questionService: QuestionService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookmarksViewModel(authRepository, siteRepository, questionService) as T
    }
}
