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
import timber.log.Timber

class QuestionDetailViewModel(
    private val service: QuestionService = ServiceProvider.questionService,
    private val authStore: AuthStore = AuthStore
) : BaseViewModel(), QuestionDetailActionHandler {

    internal val data: LiveData<List<DynamicDataModel>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicDataModel>>()

    internal val voteCount: LiveData<Int>
        get() = _voteCount
    private val _voteCount = MutableLiveData<Int>()

    private val isAuthenticated: Boolean
        get() = authStore.isAuthenticatedLiveData.value ?: false

    internal var questionId: Int = 0
    internal var question: Question? = null
    internal var isFromDeepLink = false

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

            this@QuestionDetailViewModel.question = questionResult

            _data.value = mutableListOf<DynamicDataModel>().apply {
                add(QuestionDataModel(questionResult, isDetail = true))
                if (isAuthenticated) {
                    add(
                        QuestionDetailActionDataModel(
                            this@QuestionDetailViewModel,
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
