package me.tylerbwong.stack.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.stack.api.model.Comment
import me.tylerbwong.stack.api.service.CommentService
import me.tylerbwong.stack.api.utils.toErrorResponse
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.content.ContentFilter
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.persistence.dao.CommentDraftDao
import me.tylerbwong.stack.data.persistence.entity.CommentDraftEntity
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import me.tylerbwong.stack.ui.utils.toHtml
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val service: CommentService,
    private val authRepository: AuthRepository,
    private val commentDraftDao: CommentDraftDao,
    private val siteStore: SiteStore,
    private val siteRepository: SiteRepository,
    private val contentFilter: ContentFilter,
    private val logger: Logger,
) : BaseViewModel() {

    internal val data: LiveData<List<DynamicItem>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicItem>>()

    internal val scrollToIndex: LiveData<Int>
        get() = _scrollToIndex
    private val _scrollToIndex = SingleLiveEvent<Int>()

    internal val contentFilteredUpdated: LiveData<ContentFilter.ContentFilterData>
        get() = contentFilter.contentFilteredUpdated

    val errorToast: LiveData<CommentError?>
        get() = _errorToast
    private val _errorToast = SingleLiveEvent<CommentError?>()

    private val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated

    internal var postId = -1
    internal var commentId = -1
    internal var body = ""

    @Suppress("ComplexMethod", "LongMethod")
    fun fetchComments(newComments: List<Comment> = emptyList()) {
        launchRequest {
            val commentsResponse = if (isAuthenticated) {
                service.getPostCommentsAuth(postId).also {
                    if (postId != -1) {
                        val draft = commentDraftDao.getCommentDraft(
                            postId = postId,
                            site = siteStore.site,
                        )
                        body = draft?.bodyMarkdown ?: ""
                    }
                }
            } else {
                service.getPostComments(postId)
            }
            val user = authRepository.getCurrentUser()
            val site = siteRepository.getCurrentSite()
            val result = withContext(Dispatchers.Default) {
                val finalComments = if (newComments.isNotEmpty()) {
                    commentsResponse.items.toMutableList().also {
                        it.replaceAll { existing ->
                            newComments.find { new -> new.commentId == existing.commentId }
                                ?: existing
                        }
                    }.toList()
                } else {
                    commentsResponse.items
                }
                with(contentFilter) { finalComments.applyCommentFilter() }.map {
                    CommentItem(
                        comment = it,
                        isAuthenticated = authRepository.isAuthenticated,
                        site = site,
                        siteJoinUrl = { site -> siteRepository.buildSiteJoinUrl(site) },
                        isUserPresent = { user != null },
                        hideComment = { id ->
                            contentFilter.addFilteredCommentId(id)
                            fetchComments()
                        },
                    ) { commentId, upvoteValue ->
                        toggleAction(commentId = commentId, isSelected = upvoteValue)
                    }
                } + if (isAuthenticated) {
                    listOf(
                        AddCommentItem(
                            isAuthenticated = authRepository.isAuthenticated,
                            site = site,
                            siteJoinUrl = { site -> siteRepository.buildSiteJoinUrl(site) },
                            isUserPresent = { user != null },
                            getBody = { body },
                            setBody = {
                                body = it
                                if (it.isNotEmpty()) {
                                    saveCommentDraft()
                                } else {
                                    deleteCommentDraft()
                                }
                            },
                        ) { body, isPreview ->
                            launchRequest(
                                onSuccess = {
                                    _errorToast.value = null
                                    this@CommentsViewModel.body = ""
                                    logger.logEvent(
                                        eventName = LOGGER_ADD_COMMENT_SUCCESS_EVENT_NAME,
                                        LOGGER_POST_ID_PARAM_NAME to postId.toString(),
                                    )
                                    fetchComments()
                                    deleteCommentDraft()
                                },
                                onFailure = { ex ->
                                    val errorMessage = (ex as? HttpException)?.toErrorResponse()
                                        ?.errorMessage?.toHtml()?.toString()
                                    _errorToast.postValue(
                                        errorMessage?.let {
                                            CommentError.AddCommentFailed(reason = it)
                                        } ?: CommentError.AddCommentFailed()
                                    )
                                    this@CommentsViewModel.body = body
                                    logger.logEvent(
                                        eventName = LOGGER_ADD_COMMENT_ERROR_EVENT_NAME,
                                        LOGGER_POST_ID_PARAM_NAME to postId.toString(),
                                        LOGGER_ERROR_MESSAGE_PARAM_NAME to (errorMessage ?: ""),
                                    )
                                    fetchComments()
                                },
                                block = { service.addComment(postId, body, isPreview) }
                            )
                        }
                    )
                } else {
                    emptyList()
                }
            }
            _data.value = result
            if (commentId != -1) {
                _scrollToIndex.value = result
                    .indexOfFirst { it is CommentItem && it.comment.commentId == commentId }
            }
        }
    }

    private fun saveCommentDraft() {
        if (isAuthenticated) {
            viewModelScope.launch {
                try {
                    if (postId != -1) {
                        commentDraftDao.insertCommentDraft(
                            CommentDraftEntity(
                                postId = postId,
                                bodyMarkdown = body,
                                site = siteStore.site,
                            )
                        )
                    }
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }
    }

    private fun deleteCommentDraft() {
        if (isAuthenticated) {
            viewModelScope.launch {
                try {
                    if (postId != -1) {
                        commentDraftDao.deleteDraftById(
                            postId = postId,
                            site = siteStore.site,
                        )
                    }
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }
    }

    private fun toggleAction(
        commentId: Int,
        isSelected: Boolean,
    ) {
        viewModelScope.launch {
            try {
                val result = if (isSelected) {
                    service.upvoteComment(commentId)
                } else {
                    service.undoUpvoteComment(commentId)
                }

                if (result.items.isNotEmpty()) {
                    fetchComments(result.items)
                }
            } catch (ex: HttpException) {
                ex.toErrorResponse()?.let {
                    _errorToast.postValue(
                        CommentError.UpvoteFailed(it.errorMessage.toHtml().toString())
                    )
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    sealed class CommentError {
        class AddCommentFailed(val reason: String? = null) : CommentError()
        class UpvoteFailed(val reason: String? = null) : CommentError()
    }

    companion object {
        private const val LOGGER_ADD_COMMENT_SUCCESS_EVENT_NAME = "add_comment_success"
        private const val LOGGER_ADD_COMMENT_ERROR_EVENT_NAME = "add_comment_error"
        private const val LOGGER_POST_ID_PARAM_NAME = "post_id"
        private const val LOGGER_ERROR_MESSAGE_PARAM_NAME = "error_message"
    }
}
