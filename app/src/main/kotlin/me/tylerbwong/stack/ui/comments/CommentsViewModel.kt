package me.tylerbwong.stack.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.stack.api.service.CommentService
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val service: CommentService,
    private val authStore: AuthStore
) : BaseViewModel() {

    internal val data: LiveData<List<DynamicItem>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicItem>>()

    val errorToast: LiveData<Unit?>
        get() = _errorToast
    private val _errorToast = SingleLiveEvent<Unit?>()

    private val isAuthenticated: Boolean
        get() = authStore.isAuthenticatedLiveData.value ?: false

    internal var postId = -1
    internal var initialBody = ""

    fun fetchComments() {
        launchRequest {
            val commentsResponse = if (isAuthenticated) {
                service.getPostCommentsAuth(postId)
            } else {
                service.getPostComments(postId)
            }

            _data.value = commentsResponse.items.map {
                CommentItem(it)
            } + if (isAuthenticated) {
                listOf(
                    AddCommentItem(initialBody = initialBody) { body, isPreview ->
                        launchRequest(
                            onSuccess = {
                                _errorToast.value = null
                                initialBody = ""
                                fetchComments()
                            },
                            onFailure = {
                                _errorToast.value = Unit
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
    }
}
