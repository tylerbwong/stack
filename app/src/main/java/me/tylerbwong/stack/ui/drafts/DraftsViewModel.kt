package me.tylerbwong.stack.ui.drafts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.toAnswerDraft
import me.tylerbwong.stack.ui.BaseViewModel

internal class DraftsViewModel @ViewModelInject constructor(
    private val draftsDao: AnswerDraftDao,
    private val siteStore: SiteStore
) : BaseViewModel() {

    val drafts: LiveData<List<AnswerDraft>>
        get() = _drafts
    private val _drafts = MutableLiveData<List<AnswerDraft>>()

    val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal fun fetchDrafts() {
        streamRequest(draftsDao.getAnswerDrafts(siteStore.site)) { entities ->
            _drafts.value = entities.map { it.toAnswerDraft() }
        }
    }
}
