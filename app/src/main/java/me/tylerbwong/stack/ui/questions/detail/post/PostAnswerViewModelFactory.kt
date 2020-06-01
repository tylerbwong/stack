package me.tylerbwong.stack.ui.questions.detail.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao

class PostAnswerViewModelFactory(
    private val service: QuestionService,
    private val draftDao: AnswerDraftDao,
    private val siteStore: SiteStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostAnswerViewModel(service, draftDao, siteStore) as T
    }
}
