package me.tylerbwong.stack.ui.drafts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.data.toAnswerDraft
import me.tylerbwong.stack.data.toQuestionDraft
import me.tylerbwong.stack.ui.BaseViewModel

internal class DraftsViewModel @ViewModelInject constructor(
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

    internal fun fetchDrafts() {
        streamRequest(questionDraftDao.getQuestionDrafts(siteStore.site)) { entities ->
            _questionDrafts.value = entities.map { it.toQuestionDraft() }
        }
        streamRequest(answerDraftDao.getAnswerDrafts(siteStore.site)) { entities ->
            _answerDrafts.value = entities.map { it.toAnswerDraft() }
        }
    }
}
