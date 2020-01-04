package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import me.tylerbwong.stack.ui.utils.zipWith

class QuestionDetailMainViewModel(
    authStore: AuthStore = AuthStore,
    private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

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

    internal val canAnswerQuestion = authStore.isAuthenticatedLiveData.zipWith(
        data,
        initialValue = false
    ) { isAuthenticated, data -> isAuthenticated && data.isNotEmpty() }

    internal var title = ""
    internal var isInAnswerMode = false
    internal var hasContent = false
    internal var questionId = -1
    internal var question: Question? = null

    internal fun getQuestionDetails() {
        launchRequest {
            val questionResult = service.getQuestionDetails(questionId).items.first()
            val answersResult = service.getQuestionAnswers(questionId).items.sortedBy {
                !it.isAccepted
            }

            val response = mutableListOf<QuestionDetailItem>().apply {
                add(0, QuestionItem(questionResult))
                add(AnswerHeaderItem(questionResult.answerCount))
                addAll(answersResult.map { AnswerItem(it) })
            } to questionResult

            question = response.second
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
}
