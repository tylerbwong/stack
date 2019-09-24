package me.tylerbwong.stack.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.DynamicDataModel
import me.tylerbwong.stack.ui.utils.EmptyDataModel

class CommentsViewModel(
    private val service: CommentService = ServiceProvider.commentService
) : BaseViewModel() {

    internal val data: LiveData<List<DynamicDataModel>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicDataModel>>()

    internal var postId = -1

    fun fetchComments() {
        launchRequest {
            _data.value = service.getPostComments(postId).items
                .map { CommentDataModel(it) }
                .ifEmpty { listOf(EmptyDataModel(R.string.no_comments)) }
        }
    }
}
