package me.tylerbwong.stack.ui.bookmarks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.ui.BaseViewModel

// TODO Fetch bookmarks from QuestionDao for offline
class BookmarksViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val siteRepository: SiteRepository,
    private val questionService: QuestionService
) : BaseViewModel() {

    internal val bookmarks: LiveData<List<Question>>
        get() = mutableBookmarks
    private val mutableBookmarks = MutableLiveData<List<Question>>()

    private val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated

    internal val siteLiveData: LiveData<String>
        get() = siteRepository.siteLiveData

    internal fun fetchBookmarks() {
        if (isAuthenticated) {
            launchRequest {
                mutableBookmarks.value = questionService.getBookmarks().items
            }
        }
    }
}
