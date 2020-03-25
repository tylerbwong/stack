package me.tylerbwong.stack.ui.search

import androidx.lifecycle.LiveData
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.entity.SearchEntity
import me.tylerbwong.stack.data.toSearchPayload
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent

class SearchViewModel(
    private val tagService: TagService = ServiceProvider.tagService,
    private val searchService: SearchService = ServiceProvider.searchService,
    private val searchDao: SearchDao = StackDatabase.getInstance().getSearchDao()
) : BaseViewModel() {

    internal val emptySearchData: LiveData<EmptySearchData>
        get() = _emptySearchData
    private val _emptySearchData = SingleLiveEvent<EmptySearchData>()

    internal val searchResults: LiveData<List<Question>>
        get() = _searchResults
    private val _searchResults = SingleLiveEvent<List<Question>>()

    internal var searchPayload: SearchPayload = SearchPayload.Empty

    internal fun search(searchPayload: SearchPayload = this.searchPayload) {
        this.searchPayload = searchPayload
        launchRequest {
            val result = when (searchPayload) {
                is SearchPayload.Standard -> {
                    searchDao.insert(
                        SearchEntity(
                            query = searchPayload.query,
                            isAccepted = searchPayload.isAccepted,
                            minNumAnswers = searchPayload.minNumAnswers,
                            bodyContains = searchPayload.bodyContains,
                            isClosed = searchPayload.isClosed,
                            tags = searchPayload.tags?.joinToString(";"),
                            titleContains = searchPayload.titleContains
                        )
                    )
                    searchService.search(
                        query = searchPayload.query,
                        isAccepted = searchPayload.isAccepted,
                        minNumAnswers = searchPayload.minNumAnswers,
                        bodyContains = searchPayload.bodyContains,
                        isClosed = searchPayload.isClosed,
                        tags = searchPayload.tags?.joinToString(";"),
                        titleContains = searchPayload.titleContains
                    ).items
                }
                is SearchPayload.Empty -> emptyList()
            }
            if (result.isEmpty()) {
                fetchEmptySearchData()
            } else {
                _searchResults.value = result
            }
        }
    }

    internal fun clearSearch() {
        searchPayload = SearchPayload.Empty
        launchRequest {
            fetchEmptySearchData()
        }
    }

    private suspend fun fetchEmptySearchData() {
        val tags = tagService.getPopularTags().items
        streamRequest(searchDao.getSearches()) { searchEntities ->
            _emptySearchData.value = EmptySearchData(
                tags = tags,
                searchHistory = searchEntities.map { it.toSearchPayload() }
            )
        }
    }
}
