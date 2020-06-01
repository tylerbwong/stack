package me.tylerbwong.stack.ui.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.network.service.QuestionService

class QuestionsViewModelFactory(
    private val questionService: QuestionService,
    private val siteStore: SiteStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionsViewModel(questionService, siteStore) as T
    }
}
