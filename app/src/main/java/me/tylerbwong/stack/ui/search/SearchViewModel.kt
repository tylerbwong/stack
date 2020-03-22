package me.tylerbwong.stack.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.Tag
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.ui.BaseViewModel

class SearchViewModel(
    private val tagService: TagService = ServiceProvider.tagService
) : BaseViewModel() {

    internal val tags: LiveData<List<Tag>>
        get() = _tags
    private val _tags = MutableLiveData<List<Tag>>()

    internal fun fetchTags() {
        launchRequest {
            val tagResponse = tagService.getPopularTags()
            _tags.value = tagResponse.items
        }
    }
}
