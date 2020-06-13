package me.tylerbwong.stack.ui.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import javax.inject.Inject

class DraftsViewModelFactory @Inject constructor(
    private val draftsDao: AnswerDraftDao,
    private val siteStore: SiteStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DraftsViewModel(draftsDao, siteStore) as T
    }
}
