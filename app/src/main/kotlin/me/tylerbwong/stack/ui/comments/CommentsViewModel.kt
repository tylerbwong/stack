package me.tylerbwong.stack.ui.comments

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.ui.BaseViewModel

class CommentsViewModel @ViewModelInject constructor(
    private val service: CommentService,
    private val authStore: AuthStore
) : BaseViewModel() {

    internal val data: LiveData<List<CommentItem>>
        get() = _data
    private val _data = MutableLiveData<List<CommentItem>>()

    private val isAuthenticated: Boolean
        get() = authStore.isAuthenticatedLiveData.value ?: false

    internal var postId = -1

    fun fetchComments() {
        launchRequest {
            val commentsResponse = if (isAuthenticated) {
                service.getPostCommentsAuth(postId)
            } else {
                service.getPostComments(postId)
            }

            _data.value = commentsResponse.items.map { CommentItem(it) }
        }
    }
}
