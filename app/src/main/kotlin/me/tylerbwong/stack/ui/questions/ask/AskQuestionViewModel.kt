package me.tylerbwong.stack.ui.questions.ask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.api.service.SearchService
import me.tylerbwong.stack.api.service.TagService
import me.tylerbwong.stack.api.utils.toErrorResponse
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity
import me.tylerbwong.stack.data.persistence.entity.SiteEntity
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.data.toQuestionDraft
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AskQuestionViewModel @Inject constructor(
    private val questionService: QuestionService,
    private val tagService: TagService,
    private val questionDraftDao: QuestionDraftDao,
    private val siteRepository: SiteRepository,
    private val searchService: SearchService,
) : BaseViewModel() {

    private var id: Int = -1

    val askQuestionState: LiveData<AskQuestionState>
        get() = _askQuestionState
    private val _askQuestionState = SingleLiveEvent<AskQuestionState>()

    val questionDraft: LiveData<QuestionDraft>
        get() = _questionDraft
    private val _questionDraft = MutableLiveData<QuestionDraft>()

    val title: LiveData<String>
        get() = _title
    private val _title = MutableLiveData<String>()

    val tags: LiveData<List<Tag>>
        get() = _tags
    private val _tags = MutableLiveData<List<Tag>>()

    val selectedTags: LiveData<Set<Tag>>
        get() = _selectedTags
    private val _selectedTags = MutableLiveData<Set<Tag>>()

    internal val currentSite: LiveData<Site?>
        get() = _currentSite
    private val _currentSite = MutableLiveData<Site?>()

    internal val similarQuestions: LiveData<List<Question>>
        get() = _similarQuestions
    private val _similarQuestions = MutableLiveData<List<Question>>()

    fun askQuestion(title: String, body: String, tags: String, isPreview: Boolean) {
        viewModelScope.launch {
            try {
                val response = questionService.addQuestion(
                    title,
                    body,
                    tags.replace(",", ";"),
                    preview = isPreview
                )
                _askQuestionState.value = if (isPreview) {
                    AskQuestionSuccessPreview
                } else {
                    val question = response.items.firstOrNull()
                    if (question != null) {
                        AskQuestionSuccess(question.questionId)
                    } else {
                        AskQuestionError()
                    }
                }
            } catch (ex: Exception) {
                _askQuestionState.value = AskQuestionError(
                    (ex as? HttpException)?.toErrorResponse()?.errorMessage
                )
            }
        }
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun fetchSite() {
        viewModelScope.launch {
            try {
                _currentSite.value = siteRepository.getCurrentSite()
            } catch (ex: Exception) {
                _currentSite.value = null
            }
        }
    }

    fun setCurrentSite(site: String) {
        siteRepository.changeSite(site)
        fetchSite()
    }

    fun getSites(): Flow<List<SiteEntity>> = siteRepository.getSites()

    fun fetchPopularTags(searchQuery: String) {
        if (searchQuery.isBlank()) {
            _tags.value = emptyList()
        }
        viewModelScope.launch {
            try {
                _tags.value = tagService.getPopularTags(inname = searchQuery, pageSize = 15).items
            } catch (ex: Exception) {
                _tags.value = emptyList()
            }
        }
    }

    fun setSelectedTags(tags: Set<Tag>) {
        if (tags.size <= 5) {
            _selectedTags.value = tags
        }
    }

    fun searchSimilar() {
        title.value?.let {
            viewModelScope.launch {
                try {
                    _similarQuestions.value = searchService.search(
                        query = it,
                        tags = selectedTags.value?.joinToString(";") { it.name } ?: "",
                        pageSize = 10,
                    ).items
                } catch (ex: Exception) {
                    _similarQuestions.value = emptyList()
                }
            }
        }
    }

    fun saveDraft(title: String, body: String, tags: String, timestampProvider: (Long) -> String) {
        launchRequest {
            val id = questionDraftDao.insertQuestionDraft(
                QuestionDraftEntity(
                    title,
                    System.currentTimeMillis(),
                    body,
                    tags,
                    siteRepository.site
                )
            )
            fetchDraft(id.toInt(), timestampProvider)
        }
    }

    fun deleteDraft(id: Int) {
        launchRequest {
            questionDraftDao.deleteDraftById(id, siteRepository.site)
        }
    }

    fun fetchDraft(id: Int, timestampProvider: (Long) -> String) {
        if (id != -1) {
            this.id = id
            launchRequest {
                _questionDraft.value = questionDraftDao.getQuestionDraft(id, siteRepository.site)
                    .toQuestionDraft(timestampProvider)
            }
        }
    }
}

sealed class AskQuestionState
data class AskQuestionSuccess(val questionId: Int) : AskQuestionState()
object AskQuestionSuccessPreview : AskQuestionState()
data class AskQuestionError(val errorMessage: String? = null) : AskQuestionState()
