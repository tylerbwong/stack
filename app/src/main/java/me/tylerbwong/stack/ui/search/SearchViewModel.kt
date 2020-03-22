package me.tylerbwong.stack.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.model.Tag
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.ui.BaseViewModel

class SearchViewModel(
    private val tagService: TagService = ServiceProvider.tagService,
    private val searchService: SearchService = ServiceProvider.searchService
) : BaseViewModel() {

    internal val tags: LiveData<List<Tag>>
        get() = _tags
    private val _tags = MutableLiveData<List<Tag>>()

    internal val searchResults: LiveData<List<Question>>
        get() = _searchResults
    private val _searchResults = MutableLiveData<List<Question>>()

    private var searchPayload: SearchPayload? = null

    internal fun search(searchPayload: SearchPayload? = this.searchPayload) {
        if (searchPayload != null) {
            this.searchPayload = searchPayload
            launchRequest {
                _searchResults.value = searchService.search(searchPayload.query).items
            }
        } else {
            fetchTags()
        }
    }

    internal fun clearSearch() {
        searchPayload = null
        fetchTags()
    }

    private fun fetchTags() {
        launchRequest {
            val tagResponse = tagService.getPopularTags()
            _tags.value = tagResponse.items
        }
    }
}
