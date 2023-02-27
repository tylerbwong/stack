package me.tylerbwong.stack.ui.questions.ask

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.api.service.SearchService
import me.tylerbwong.stack.api.service.TagService
import me.tylerbwong.stack.api.utils.toErrorResponse
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity
import me.tylerbwong.stack.data.persistence.entity.SiteEntity
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import retrofit2.HttpException
import timber.log.Timber
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
    private val _askQuestionState = MutableLiveData<AskQuestionState>()

    val draftStatus: LiveData<DraftStatus>
        get() = _draftStatus
    private val _draftStatus = SingleLiveEvent<DraftStatus>()

    var title by mutableStateOf(value = "")
    var body by mutableStateOf(value = "")
    var expandBody by mutableStateOf(value = "")
    var selectedTags by mutableStateOf(value = emptySet<Tag>())
    var isReviewed by mutableStateOf(value = false)

    val tags: LiveData<List<Tag>>
        get() = _tags
    private val _tags = MutableLiveData<List<Tag>>()

    internal val currentSite: LiveData<Site?>
        get() = _currentSite
    private val _currentSite = MutableLiveData<Site?>()

    internal val similarQuestions: LiveData<List<Question>>
        get() = _similarQuestions
    private val _similarQuestions = MutableLiveData<List<Question>>()

    val hasActiveDraft: Boolean
        get() = id != -1

    /**
     * Even if content is blank, if we already have an active draft save the draft anyways
     */
    val shouldSaveDraft: Boolean
        get() = listOf(
            { title.isNotBlank() },
            { body.isNotBlank() },
            { expandBody.isNotBlank() },
            { selectedTags.isNotEmpty() },
        ).any { it() } || hasActiveDraft

    fun askQuestion() {
        viewModelScope.launch {
            try {
                _askQuestionState.value = AskQuestionState.Posting
                val response = questionService.addQuestion(
                    title = title.trim(),
                    body = listOf(body, expandBody).joinToString("\n\n").trim(),
                    tags = selectedTags.joinToString(";") { it.name },
                    preview = BuildConfig.DEBUG,
                )
                val question = response.items.firstOrNull()
                _askQuestionState.value = if (question != null) {
                    if (BuildConfig.DEBUG) {
                        AskQuestionState.SuccessPreview
                    } else {
                        questionDraftDao.deleteDraftById(id, siteRepository.site)
                        AskQuestionState.Success(question.questionId)
                    }
                } else {
                    AskQuestionState.Error()
                }
            } catch (ex: Exception) {
                _askQuestionState.value = AskQuestionState.Error(
                    (ex as? HttpException)?.toErrorResponse()?.errorMessage
                )
            }
        }
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

    fun searchSimilar() {
        viewModelScope.launch {
            try {
                _similarQuestions.value = searchService.search(
                    query = title,
                    tags = selectedTags.joinToString(";") { it.name },
                    pageSize = 10,
                ).items
            } catch (ex: Exception) {
                _similarQuestions.value = emptyList()
            }
        }
    }

    fun saveDraft() {
        viewModelScope.launch {
            try {
                _draftStatus.value = DraftStatus.Saving
                val id = questionDraftDao.insertQuestionDraft(
                    QuestionDraftEntity(
                        title = title,
                        System.currentTimeMillis(),
                        body = body,
                        expandBody = expandBody,
                        selectedTags.joinToString(";") { it.name },
                        siteRepository.site
                    ).also {
                        if (this@AskQuestionViewModel.id != -1) {
                            it.id = this@AskQuestionViewModel.id
                        }
                    }
                )
                this@AskQuestionViewModel.id = id.toInt()
                delay(1_000)
                _draftStatus.value = DraftStatus.Complete
            } catch (exception: Exception) {
                delay(1_000)
                _draftStatus.value = DraftStatus.Failed
                Timber.e(exception)
            }
        }
    }

    fun deleteDraft() {
        launchRequest {
            questionDraftDao.deleteDraftById(id, siteRepository.site)
            title = ""
            body = ""
            expandBody = ""
            selectedTags = emptySet()
            isReviewed = false
            _askQuestionState.value = AskQuestionState.DraftDeleted
        }
    }

    fun fetchDraft(id: Int) {
        if (id != -1) {
            this.id = id
            launchRequest {
                val draft = questionDraftDao.getQuestionDraft(id, siteRepository.site)
                title = draft.title
                body = draft.body
                expandBody = draft.expandBody
                selectedTags = tagService.getTagsInfo(
                    tags = draft.tags
                ).items.filter { it.name in draft.tags }.toSet()
                isReviewed = false
            }
        }
    }
}

sealed class AskQuestionState {
    object Idle : AskQuestionState()
    object DraftDeleted : AskQuestionState()
    object Posting : AskQuestionState()
    data class Success(val questionId: Int) : AskQuestionState()
    object SuccessPreview : AskQuestionState()
    data class Error(val errorMessage: String? = null) : AskQuestionState()
}

sealed class DraftStatus {
    object Idle : DraftStatus()
    object Saving : DraftStatus()
    object Complete : DraftStatus()
    object Failed : DraftStatus()

    companion object {
        fun values(): Set<DraftStatus> = setOf(Idle, Saving, Complete, Failed)
    }
}
