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
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.content.ContentFilter
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import me.tylerbwong.stack.ui.utils.toHtml
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val service: CommentService,
    private val authStore: AuthStore,
    private val contentFilter: ContentFilter,
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
        get() = authStore.isAuthenticatedLiveData.value ?: false

    internal var postId = -1
    internal var commentId = -1
    internal var initialBody = ""

    fun fetchComments(newComments: List<Comment> = emptyList()) {
        launchRequest {
            val commentsResponse = if (isAuthenticated) {
                service.getPostCommentsAuth(postId)
            } else {
                service.getPostComments(postId)
            }

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
                        hideComment = { id ->
                            contentFilter.addFilteredCommentId(id)
                            fetchComments()
                        },
                    ) { commentId, upvoteValue ->
                        toggleAction(commentId = commentId, isSelected = upvoteValue)
                    }
                } + if (isAuthenticated) {
                    listOf(
                        AddCommentItem(initialBody = initialBody) { body, isPreview ->
                            launchRequest(
                                onSuccess = {
                                    _errorToast.value = null
                                    initialBody = ""
                                    fetchComments()
                                },
                                onFailure = { ex ->
                                    _errorToast.postValue(
                                        (ex as? HttpException)?.toErrorResponse()?.let {
                                            CommentError.AddCommentFailed(
                                                reason = it.errorMessage.toHtml().toString()
                                            )
                                        } ?: CommentError.AddCommentFailed()
                                    )
                                    initialBody = body
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
}
