package me.tylerbwong.stack.ui.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.merge
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.persistence.entity.AnswerDraftEntity
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.data.toAnswerDraft
import me.tylerbwong.stack.data.toQuestionDraft
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.home.AnswerDraftItem
import me.tylerbwong.stack.ui.home.QuestionDraftItem
import javax.inject.Inject

@HiltViewModel
internal class DraftsViewModel @Inject constructor(
    private val answerDraftDao: AnswerDraftDao,
    private val questionDraftDao: QuestionDraftDao,
    private val siteStore: SiteStore
) : BaseViewModel() {

    val drafts: LiveData<List<DynamicItem>>
        get() = _drafts
    private val _drafts = MutableLiveData<List<DynamicItem>>()

    val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal fun fetchDrafts(timestampProvider: (Long) -> String) {
        streamRequest(
            merge(
                questionDraftDao.getQuestionDrafts(siteStore.site),
                answerDraftDao.getAnswerDrafts(siteStore.site),
            )
        ) { drafts ->
            _drafts.value = drafts.mapNotNull { draft ->
                when (draft) {
                    is AnswerDraftEntity -> AnswerDraftItem(draft.toAnswerDraft(timestampProvider))
                    is QuestionDraftEntity -> QuestionDraftItem(draft.toQuestionDraft(timestampProvider))
                    else -> null
                }
            }
        }
    }
}
