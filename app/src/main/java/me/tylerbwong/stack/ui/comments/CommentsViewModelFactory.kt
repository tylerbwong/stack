package me.tylerbwong.stack.ui.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.service.CommentService

class CommentsViewModelFactory(
    private val service: CommentService,
    private val authStore: AuthStore,
    private val siteStore: SiteStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommentsViewModel(service, authStore, siteStore) as T
    }
}
