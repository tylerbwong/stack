package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.answers.AnswerDataModel
import me.tylerbwong.stack.ui.questions.QuestionDataModel
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class QuestionDetailViewModel(
        private val service: QuestionService = ServiceProvider.questionService
) : BaseViewModel() {

    internal val data: LiveData<List<DynamicDataModel>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicDataModel>>()

    internal val voteCount: LiveData<Int>
        get() = _voteCount
    private val _voteCount = MutableLiveData<Int>()

    internal var questionId: Int = 0
    internal var question: Question? = null
    internal var isFromDeepLink = false

    internal fun getQuestionDetails() {
        launchRequest {
            val response = withContext(Dispatchers.IO) {
                val question = service.getQuestionDetails(questionId).await()
                        .items.first()
                val answers = service.getQuestionAnswers(questionId).await()
                        .items.sortedBy { !it.isAccepted }
                mutableListOf<DynamicDataModel>().apply {
                    add(0, QuestionDataModel(question, isDetail = true))
                    add(AnswerHeaderDataModel(question.answerCount))
                    addAll(answers.map { AnswerDataModel(it) })
                } to question
            }

            question = response.second

            _data.value = response.first
            _voteCount.value = response.second.upVoteCount - response.second.downVoteCount
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

    companion object {
        private const val SHARE_TEXT_TYPE = "text/plain"
    }
}
