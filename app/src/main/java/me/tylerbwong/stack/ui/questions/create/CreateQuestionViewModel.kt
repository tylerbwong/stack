package me.tylerbwong.stack.ui.questions.create

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity
import me.tylerbwong.stack.data.toQuestionDraft
import me.tylerbwong.stack.ui.BaseViewModel

class CreateQuestionViewModel @ViewModelInject constructor(
    private val questionService: QuestionService,
    private val questionDraftDao: QuestionDraftDao,
    private val siteStore: SiteStore
) : BaseViewModel() {

    private var id: Int = -1

    val questionDraft: LiveData<QuestionDraft>
        get() = _questionDraft
    private val _questionDraft = MutableLiveData<QuestionDraft>()

    fun createQuestion(title: String, body: String, tags: String, isPreview: Boolean) {
        launchRequest {
            questionService.addQuestion(
                title,
                body,
                tags.replace(",", ";"),
                preview = isPreview
            )
        }
    }

    fun saveDraft(title: String, body: String, tags: String) {
        launchRequest {
            val id = questionDraftDao.insertQuestionDraft(
                QuestionDraftEntity(title, System.currentTimeMillis(), body, tags, siteStore.site)
            )
            fetchDraft(id.toInt())
        }
    }

    fun deleteDraft(id: Int) {
        launchRequest {
            questionDraftDao.deleteDraftById(id, siteStore.site)
        }
    }

    fun fetchDraft(id: Int) {
        if (id != -1) {
            this.id = id
            launchRequest {
                _questionDraft.value = questionDraftDao.getQuestionDraft(id, siteStore.site)
                    .toQuestionDraft()
            }
        }
    }
}
