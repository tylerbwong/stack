package me.tylerbwong.stack.ui.search

import androidx.lifecycle.LiveData
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.model.Tag
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent

class SearchViewModel(
    private val tagService: TagService = ServiceProvider.tagService,
    private val searchService: SearchService = ServiceProvider.searchService
) : BaseViewModel() {

    internal val tags: LiveData<List<Tag>>
        get() = _tags
    private val _tags = SingleLiveEvent<List<Tag>>()

    internal val searchResults: LiveData<List<Question>>
        get() = _searchResults
    private val _searchResults = SingleLiveEvent<List<Question>>()

    internal var searchPayload: SearchPayload = SearchPayload.Empty

    internal fun search(searchPayload: SearchPayload = this.searchPayload) {
        this.searchPayload = searchPayload
        launchRequest {
            val result = when (searchPayload) {
                is SearchPayload.Advanced -> {
                    searchService.search(
                        query = searchPayload.query,
                        isAccepted = searchPayload.isAccepted,
                        minNumAnswers = searchPayload.minNumAnswers,
                        bodyContains = searchPayload.bodyContains,
                        isClosed = searchPayload.isClosed,
                        tags = searchPayload.tags,
                        titleContains = searchPayload.titleContains
                    ).items
                }
                is SearchPayload.Basic -> searchService.search(query = searchPayload.query).items
                is SearchPayload.Empty -> emptyList()
            }
            if (result.isEmpty()) {
                fetchTags()
            } else {
                _searchResults.value = result
            }
        }
    }

    internal fun clearSearch() {
        searchPayload = SearchPayload.Empty
        launchRequest {
            fetchTags()
        }
    }

    private suspend fun fetchTags() {
        val tagResponse = tagService.getPopularTags()
        _tags.value = tagResponse.items
    }
}
