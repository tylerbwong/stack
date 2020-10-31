package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.api.utils.toErrorResponse
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import me.tylerbwong.stack.ui.utils.toHtml
import me.tylerbwong.stack.ui.utils.zipWith
import retrofit2.HttpException
import timber.log.Timber

class QuestionDetailMainViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val questionRepository: QuestionRepository,
    private val service: QuestionService
) : BaseViewModel(), QuestionDetailActionHandler {

    internal val data: LiveData<List<QuestionDetailItem>>
        get() = _data
    private val _data = MutableLiveData<List<QuestionDetailItem>>()

    internal val scrollToIndex: LiveData<Int>
        get() = _scrollToIndex
    private val _scrollToIndex = SingleLiveEvent<Int>()

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

    internal val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated

    internal val canAnswerQuestion = authRepository.isAuthenticatedLiveData.zipWith(
        data,
        initialValue = false
    ) { isAuthenticated, data -> isAuthenticated && data.isNotEmpty() }

    internal var title = ""
    internal var isInAnswerMode = false
    internal var hasContent = false
    internal var questionId = -1
    internal var answerId = -1
    internal var question: Question? = null

    internal fun getQuestionDetails(question: Question? = null) {
        launchRequest {
            val questionResult = question ?: questionRepository.getQuestion(questionId)
            val answersResult = questionRepository.getQuestionAnswers(questionId)

            val response = mutableListOf<QuestionDetailItem>().apply {
                add(0, QuestionMainItem(questionResult))
                if (isAuthenticated) {
                    add(QuestionActionItem(this@QuestionDetailMainViewModel, questionResult))
                }
                add(AnswerHeaderItem(questionResult.answerCount))
                addAll(answersResult.map { AnswerItem(it) })
            } to questionResult

            this@QuestionDetailMainViewModel.question = response.second
            _liveQuestion.value = response.second

            _data.value = response.first
            _voteCount.value = response.second.upVoteCount - response.second.downVoteCount

            if (answerId != -1) {
                _scrollToIndex.value = response.first
                    .indexOfFirst { it is AnswerItem && it.answer.answerId == answerId }
            }
        }
    }

    internal fun clearFields() {
        _clearFields.value = Unit
    }

    internal fun startShareIntent(context: Context) {
        question?.let {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = SHARE_TEXT_TYPE
                putExtra(Intent.EXTRA_SUBJECT, it.title.toHtml())
                putExtra(Intent.EXTRA_TEXT, it.shareLink)
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }
    }

    override fun toggleDownvote(isSelected: Boolean) {
        toggleAction(
            isSelected,
            { service.downvoteQuestionById(it) },
            { service.undoQuestionDownvoteById(it) }
        )
    }

    override fun toggleFavorite(isSelected: Boolean) {
        toggleAction(
            isSelected,
            {
                val response = service.favoriteQuestionById(it)
                withContext(Dispatchers.IO) {
                    val question = response.items.firstOrNull()
                    if (question != null) {
                        questionRepository.saveQuestion(question)
                    }
                }
                response
            },
            {
                val response = service.undoQuestionFavoriteById(it)
                withContext(Dispatchers.IO) {
                    val question = response.items.firstOrNull()
                    if (question != null) {
                        questionRepository.removeQuestion(question)
                    }
                }
                response
            }
        )
    }

    override fun toggleUpvote(isSelected: Boolean) {
        toggleAction(
            isSelected,
            { service.upvoteQuestionById(it) },
            { service.undoQuestionUpvoteById(it) }
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
