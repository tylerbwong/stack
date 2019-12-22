package me.tylerbwong.stack.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.model.Comment
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.ui.BaseViewModel

class CommentsViewModel(
    private val service: CommentService = ServiceProvider.commentService,
    private val authStore: AuthStore = AuthStore
) : BaseViewModel() {

    internal val data: LiveData<List<Comment>>
        get() = _data
    private val _data = MutableLiveData<List<Comment>>()

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

            _data.value = commentsResponse.items
        }
    }
}
