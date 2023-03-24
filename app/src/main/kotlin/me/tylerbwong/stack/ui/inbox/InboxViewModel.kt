package me.tylerbwong.stack.ui.inbox

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.api.model.InboxItem
import me.tylerbwong.stack.api.service.AnswerService
import me.tylerbwong.stack.data.repository.ALL
import me.tylerbwong.stack.data.repository.InboxFilter
import me.tylerbwong.stack.data.repository.InboxRepository
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.launchUrl
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val inboxRepository: InboxRepository,
    private val answerService: AnswerService,
) : BaseViewModel() {

    val inboxItems: LiveData<List<InboxItem>>
        get() = inboxRepository.inboxItems

    val unreadCount: LiveData<Int>
        get() = inboxRepository.unreadCount

    private var currentFilter = ALL

    fun fetchInbox(@InboxFilter filter: String = currentFilter) {
        currentFilter = filter
        launchRequest {
            inboxRepository.fetchInbox(filter)
        }
    }

    fun onItemClicked(context: Context, inboxItem: InboxItem) {
        launchRequest {
            // We need the original questionId to be able to deep link to the right place
            // We fetch here to avoid rate-limiting by fetching all at once
            (inboxItem.questionId ?: getQuestionIdByAnswerId(inboxItem))?.let {
                val intent = QuestionDetailActivity.makeIntent(
                    context = context,
                    questionId = it,
                    answerId = inboxItem.answerId,
                    commentId = inboxItem.commentId,
                    deepLinkSite = inboxItem.site?.parameter,
                )
                context.startActivity(intent)
            } ?: context.launchUrl(inboxItem.link)
        }
    }

    private suspend fun getQuestionIdByAnswerId(inboxItem: InboxItem): Int? {
        val answerId = inboxItem.answerId
        return try {
            if (answerId != null) {
                withContext(Dispatchers.IO) {
                    answerService
                        .getAnswerByIdAuth(answerId = answerId, site = inboxItem.site?.parameter)
                        .items
                        .firstOrNull()
                        ?.questionId
                }
            } else {
                null
            }
        } catch (ex: Exception) {
            null
        }
    }
}
