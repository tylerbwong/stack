package me.tylerbwong.stack.ui.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel

// TODO Fetch bookmarks from QuestionDao for offline
class BookmarksViewModel(private val questionService: QuestionService) : BaseViewModel() {

    internal val bookmarks: LiveData<List<Question>>
        get() = mutableBookmarks
    private val mutableBookmarks = MutableLiveData<List<Question>>()

    internal fun fetchBookmarks() {
        launchRequest {
            mutableBookmarks.value = questionService.getBookmarks().items
        }
    }
}
