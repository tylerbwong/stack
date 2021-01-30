package me.tylerbwong.stack.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.service.CommentService
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
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
