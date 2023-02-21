package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.api.utils.ERROR_ID_INVALID_ACCESS_TOKEN
import me.tylerbwong.stack.api.utils.toErrorResponse
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.markdown.Markdown
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import me.tylerbwong.stack.ui.utils.toHtml
import me.tylerbwong.stack.ui.utils.zipWith
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.IndentedCodeBlock
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionDetailMainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val siteRepository: SiteRepository,
    private val questionRepository: QuestionRepository,
    private val service: QuestionService,
    private val markdown: Markdown,
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

    val showRegisterDialog: LiveData<Unit>
        get() = _showRegisterDialog
    private val _showRegisterDialog = SingleLiveEvent<Unit>()

    val showCopyDialog: LiveData<CopyData>
        get() = _showCopyDialog
    private val _showCopyDialog = SingleLiveEvent<CopyData>()

    internal val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated

    internal val user: LiveData<User?>
        get() = _user
    private val _user = MutableLiveData<User?>()

    internal val site: LiveData<Site>
        get() = _site
    private val _site = MutableLiveData<Site>()

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

    internal fun buildSiteJoinUrl(site: Site): String = siteRepository.buildSiteJoinUrl(site)

    @Suppress("LongMethod", "ComplexMethod")
    internal fun getQuestionDetails(question: Question? = null) {
        launchRequest {
            val questionResult = question ?: questionRepository.getQuestion(questionId)
            val answersResult = questionRepository.getQuestionAnswers(questionId)
            val questionLongClickListener = {
                questionResult.bodyMarkdown?.let { bodyMarkdown ->
                    viewModelScope.launch {
                        val copyData = withContext(Dispatchers.Default) {
                            CopyData(
                                titleText = questionResult.title.toHtml().toString(),
                                bodyText = markdown.render(bodyMarkdown).toString(),
                                bodyMarkdown = bodyMarkdown,
                                linkText = questionResult.shareLink,
                            )
                        }
                        _showCopyDialog.value = copyData
                    }
                }
                Unit
            }

            val detailItems = withContext(Dispatchers.Default) {
                mutableListOf<QuestionDetailItem>().apply {
                    add(0, QuestionTitleItem(questionResult.title, questionLongClickListener))
                    questionResult.closedDetails?.let { closedDetails ->
                        if (closedDetails.hasReason) {
                            questionResult.closedDate?.let { closedDate ->
                                add(QuestionNoticeItem(closedDetails, closedDate))
                            }
                        }
                    }
                    addAll(
                        collectMarkdownItems(questionResult.bodyMarkdown).also {
                            it.filterIsInstance<BaseMarkdownItem>().forEach { item ->
                                item.render(markdown)
                                item.onLongPress = questionLongClickListener
                            }
                        }
                    )
                    questionResult.tags?.let { add(QuestionTagsItem(it)) }
                    add(
                        FooterItem(
                            entityId = questionResult.questionId,
                            creationDate = questionResult.creationDate,
                            creationResId = R.string.asked,
                            lastEditor = questionResult.lastEditor,
                            commentCount = questionResult.commentCount,
                            owner = questionResult.owner,
                        )
                    )
                    if (isAuthenticated) {
                        add(QuestionActionItem(this@QuestionDetailMainViewModel, questionResult))
                    }
                    add(AnswerHeaderItem(questionResult.answerCount))
                    addAll(
                        answersResult.flatMapIndexed { index, answer ->
                            mutableListOf<QuestionDetailItem>().apply {
                                add(
                                    AnswerVotesHeaderItem(
                                        id = answer.answerId,
                                        isAccepted = answer.isAccepted,
                                        upVoteCount = answer.upVoteCount,
                                        downVoteCount = answer.downVoteCount
                                    )
                                )
                                addAll(
                                    collectMarkdownItems(answer.bodyMarkdown).also {
                                        it.filterIsInstance<BaseMarkdownItem>().forEach { item ->
                                            item.render(markdown)
                                            item.onLongPress = {
                                                viewModelScope.launch {
                                                    val copyData = withContext(Dispatchers.Default) {
                                                        CopyData(
                                                            titleText = null,
                                                            bodyText = markdown.render(answer.bodyMarkdown).toString(),
                                                            bodyMarkdown = answer.bodyMarkdown,
                                                            linkText = answer.shareLink,
                                                        )
                                                    }
                                                    _showCopyDialog.value = copyData
                                                }
                                            }
                                        }
                                    }
                                )
                                add(
                                    FooterItem(
                                        entityId = answer.answerId,
                                        creationDate = answer.creationDate,
                                        creationResId = R.string.answered,
                                        lastEditor = answer.lastEditor,
                                        commentCount = answer.commentCount,
                                        owner = answer.owner,
                                    )
                                )
                                if (index != answersResult.lastIndex) {
                                    add(DividerItem)
                                }
                            }
                        }
                    )
                }
            }

            this@QuestionDetailMainViewModel.question = questionResult
            _liveQuestion.value = questionResult

            _data.value = detailItems
            _voteCount.value = questionResult.upVoteCount - questionResult.downVoteCount

            if (answerId != -1) {
                _scrollToIndex.value = detailItems
                    .indexOfFirst { it is AnswerVotesHeaderItem && it.id == answerId }
            }
            _user.value = authRepository.getCurrentUser()
            _site.value = siteRepository.getCurrentSite()
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
                // TODO Enable Offline
//                val response = service.favoriteQuestionById(it)
//                withContext(Dispatchers.IO) {
//                    val question = response.items.firstOrNull()
//                    if (question != null) {
//                        questionRepository.saveQuestion(question)
//                    }
//                }
//                response
                service.favoriteQuestionById(it)
            },
            {
                // TODO Enable Offline
//                val response = service.undoQuestionFavoriteById(it)
//                withContext(Dispatchers.IO) {
//                    val question = response.items.firstOrNull()
//                    if (question != null) {
//                        questionRepository.removeQuestion(question)
//                    }
//                }
//                response
                service.undoQuestionFavoriteById(it)
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
                ex.toErrorResponse()?.let {
                    if (it.errorId == ERROR_ID_INVALID_ACCESS_TOKEN) {
                        _showRegisterDialog.value = Unit
                    } else {
                        mutableMessageSnackbar.postValue(it.errorMessage.toHtml().toString())
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    private fun collectMarkdownItems(markdownContent: String?): List<QuestionDetailItem> {
        val rootNode = markdownContent?.let { markdown.parse(it) }
        val nodes = rootNode
            ?.let { markdown.reduce(it) }
            ?: emptyList()
        return nodes.map { node ->
            if (node is FencedCodeBlock || node is IndentedCodeBlock) {
                FencedCodeBlockItem(node)
            } else {
                MarkdownItem(node)
            }
        }
    }

    data class CopyData(
        val titleText: String?,
        val bodyText: String,
        val bodyMarkdown: String,
        val linkText: String,
    )
}
