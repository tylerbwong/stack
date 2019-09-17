package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.answers.AnswerDataModel
import me.tylerbwong.stack.ui.questions.QuestionDataModel
import me.tylerbwong.stack.ui.utils.DynamicDataModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import me.tylerbwong.stack.ui.utils.zipWith
import timber.log.Timber

class QuestionDetailMainViewModel(
    private val authStore: AuthStore = AuthStore,
    private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel(), QuestionDetailActionHandler {

    internal val data: LiveData<List<DynamicDataModel>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicDataModel>>()

    internal val voteCount: LiveData<Int>
        get() = _voteCount
    private val _voteCount = MutableLiveData<Int>()

    internal val clearFields: LiveData<Unit>
        get() = _clearFields
    private val _clearFields = SingleLiveEvent<Unit>()

    private val isAuthenticated: Boolean
        get() = authStore.isAuthenticatedLiveData.value ?: false

    internal val canAnswerQuestion = authStore.isAuthenticatedLiveData.zipWith(
        data,
        initialValue = false
    ) { isAuthenticated, data -> isAuthenticated && data.isNotEmpty() }

    internal var title = ""
    internal var isInAnswerMode = false
    internal var hasContent = false
    internal var questionId = -1
    internal var question: Question? = null

    internal fun getQuestionDetails(question: Question? = null) {
        launchRequest {
            val questionResult = question ?: if (isAuthenticated) {
                service.getQuestionDetailsAuth(questionId).items.first()
            } else {
                service.getQuestionDetails(questionId).items.first()
            }
            val answersResult = service.getQuestionAnswers(questionId).items.sortedBy {
                !it.isAccepted
            }

            this@QuestionDetailMainViewModel.question = questionResult

            _data.value = mutableListOf<DynamicDataModel>().apply {
                add(QuestionDataModel(questionResult, isDetail = true))
                if (isAuthenticated) {
                    add(
                        QuestionDetailActionDataModel(
                            this@QuestionDetailMainViewModel,
                            questionResult
                        )
                    )
                }
                add(AnswerHeaderDataModel(questionResult.answerCount))
                addAll(answersResult.map { AnswerDataModel(it) })
            }
            _voteCount.value = questionResult.upVoteCount - questionResult.downVoteCount
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
            { service.favoriteQuestionById(it) },
            { service.undoQuestionFavoriteById(it) }
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
            } catch (ex: Exception) {
                Timber.e(ex)
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
                putExtra(Intent.EXTRA_SUBJECT, it.title)
                putExtra(Intent.EXTRA_TEXT, it.shareLink)
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }
    }
}
