package me.tylerbwong.stack.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.network.service.QuestionService

class BookmarksViewModelFactory(
    private val questionService: QuestionService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookmarksViewModel(questionService) as T
    }
}
