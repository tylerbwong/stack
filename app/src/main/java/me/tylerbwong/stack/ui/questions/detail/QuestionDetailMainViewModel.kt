package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import me.tylerbwong.stack.ui.utils.toErrorResponse
import me.tylerbwong.stack.ui.utils.zipWith
import retrofit2.HttpException
import timber.log.Timber

class QuestionDetailMainViewModel(
    private val authRepository: AuthRepository,
    private val siteStore: SiteStore,
    private val service: QuestionService
) : BaseViewModel(), QuestionDetailActionHandler {

    internal val data: LiveData<List<QuestionDetailItem>>
        get() = _data
    private val _data = MutableLiveData<List<QuestionDetailItem>>()

    internal val liveQuestion: LiveData<Question>
        get() = _liveQuestion
    private val _liveQuestion = MutableLiveData<Question>()

    internal val voteCount: LiveData<Int>
        get() = _voteCount
    private val _voteCount = MutableLiveData<Int>()

    internal val clearFields: LiveData<Unit>
        get() = _clearFields
    private val _clearFields = SingleLiveEvent<Unit>()

    val messageSnackbar: LiveData<String>
        get() = mutableMessageSnackbar
    private val mutableMessageSnackbar = SingleLiveEvent<String>()

    private val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated.value ?: false

    internal val canAnswerQuestion = authRepository.isAuthenticated.zipWith(
        data,
        initialValue = false
    ) { isAuthenticated, data -> isAuthenticated && data.isNotEmpty() }

    internal val isInCurrentSite: Boolean
        get() = site == null || site == siteStore.site

    internal var title = ""
    internal var isInAnswerMode = false
    internal var hasContent = false
    internal var questionId = -1
    internal var site: String? = null
    internal var question: Question? = null

    internal fun getQuestionDetails(question: Question? = null) {
        val site = site ?: siteStore.site
        launchRequest {
            val questionResult = question ?: if (isAuthenticated) {
                service.getQuestionDetailsAuth(questionId, site).items.first()
            } else {
                service.getQuestionDetails(questionId, site).items.first()
            }
            val answersResult = service.getQuestionAnswers(questionId, site).items.sortedBy {
                !it.isAccepted
            }

            val response = mutableListOf<QuestionDetailItem>().apply {
                add(0, QuestionItem(questionResult, isInCurrentSite))
                if (isAuthenticated) {
                    add(QuestionActionItem(this@QuestionDetailMainViewModel, questionResult))
                }
                add(AnswerHeaderItem(questionResult.answerCount))
                addAll(answersResult.map { AnswerItem(it, site, isInCurrentSite) })
            } to questionResult

            this@QuestionDetailMainViewModel.question = response.second
            _liveQuestion.value = response.second

            _data.value = response.first
            _voteCount.value = response.second.upVoteCount - response.second.downVoteCount
        }
    }

    internal fun clearFields() {
        _clearFields.value = Unit
    }

    internal fun startShareIntent(context: Context) {
        question?.let {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = SHARE_TEXT_TYPE
                putExtra(Intent.EXTRA_SUBJECT, it.title)
                putExtra(Intent.EXTRA_TEXT, it.shareLink)
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }
    }

    override fun toggleDownvote(isSelected: Boolean) {
        val site = site ?: siteStore.site
        toggleAction(
            isSelected,
            { service.downvoteQuestionById(it, site) },
            { service.undoQuestionDownvoteById(it, site) }
        )
    }

    override fun toggleFavorite(isSelected: Boolean) {
        val site = site ?: siteStore.site
        toggleAction(
            isSelected,
            { service.favoriteQuestionById(it, site) },
            { service.undoQuestionFavoriteById(it, site) }
        )
    }

    override fun toggleUpvote(isSelected: Boolean) {
        val site = site ?: siteStore.site
        toggleAction(
            isSelected,
            { service.upvoteQuestionById(it, site) },
            { service.undoQuestionUpvoteById(it, site) }
        )
    }

    private fun toggleAction(
        isSelected: Boolean,
        selectedAction: suspend (Int) -> Response<Question>,
        unselectedAction: suspend (Int) -> Response<Question>
    ) {
        viewModelScope.launch {
            try {
                val result = if (isSelected) {
                    selectedAction(questionId)
                } else {
                    unselectedAction(questionId)
                }

                if (result.items.isNotEmpty()) {
                    getQuestionDetails(result.items.firstOrNull())
                }
            } catch (ex: HttpException) {
                try {
                    val errorResponse = ex.toErrorResponse()
                    if (errorResponse != null) {
                        mutableMessageSnackbar.postValue(errorResponse.errorMessage)
                    }
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }
    }
}
