package me.tylerbwong.stack.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.ui.BaseViewModel

class CommentsViewModel(
    private val service: CommentService,
    private val authStore: AuthStore,
    private val siteStore: SiteStore
) : BaseViewModel() {

    internal val data: LiveData<List<CommentItem>>
        get() = _data
    private val _data = MutableLiveData<List<CommentItem>>()

    private val isAuthenticated: Boolean
        get() = authStore.isAuthenticatedLiveData.value ?: false

    private val isInCurrentSite: Boolean
        get() = site == null || site == siteStore.site

    internal var postId = -1
    internal var site: String? = null

    fun fetchComments() {
        launchRequest {
            val commentsResponse = if (isAuthenticated) {
                service.getPostCommentsAuth(postId, site = site ?: siteStore.site)
            } else {
                service.getPostComments(postId, site = site ?: siteStore.site)
            }

            _data.value = commentsResponse.items.map { CommentItem(it, isInCurrentSite) }
        }
    }
}
