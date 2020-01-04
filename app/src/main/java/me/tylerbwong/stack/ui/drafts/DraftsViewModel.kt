package me.tylerbwong.stack.ui.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.toAnswerDraft
import me.tylerbwong.stack.ui.BaseViewModel

internal class DraftsViewModel(
    private val draftsDao: AnswerDraftDao = StackDatabase.getInstance().getAnswerDraftDao()
) : BaseViewModel() {

    val drafts: LiveData<List<AnswerDraft>>
        get() = _drafts
    private val _drafts = MutableLiveData<List<AnswerDraft>>()

    internal fun fetchDrafts() {
        streamRequest(draftsDao.getAnswerDrafts()) { entities ->
            _drafts.value = entities.map { it.toAnswerDraft() }
        }
    }
}
