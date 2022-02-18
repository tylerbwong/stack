package me.tylerbwong.stack.ui.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.data.toAnswerDraft
import me.tylerbwong.stack.data.toQuestionDraft
import me.tylerbwong.stack.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class DraftsViewModel @Inject constructor(
    private val answerDraftDao: AnswerDraftDao,
    private val questionDraftDao: QuestionDraftDao,
    private val siteStore: SiteStore
) : BaseViewModel() {

    val questionDrafts: LiveData<List<QuestionDraft>>
        get() = _questionDrafts
    private val _questionDrafts = MutableLiveData<List<QuestionDraft>>()

    val answerDrafts: LiveData<List<AnswerDraft>>
        get() = _answerDrafts
    private val _answerDrafts = MutableLiveData<List<AnswerDraft>>()

    val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal fun fetchDrafts(timestampProvider: (Long) -> String) {
        streamRequest(questionDraftDao.getQuestionDrafts(siteStore.site)) { entities ->
            _questionDrafts.value = entities.map { it.toQuestionDraft(timestampProvider) }
        }
        streamRequest(answerDraftDao.getAnswerDrafts(siteStore.site)) { entities ->
            _answerDrafts.value = entities.map { it.toAnswerDraft(timestampProvider) }
        }
    }
}
