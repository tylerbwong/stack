package me.tylerbwong.stack.ui.search

import androidx.lifecycle.LiveData
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.entity.SearchEntity
import me.tylerbwong.stack.data.toSearchPayload
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent

class SearchViewModel(
    private val tagService: TagService,
    private val searchService: SearchService,
    private val searchDao: SearchDao,
    private val siteStore: SiteStore
) : BaseViewModel() {

    internal val emptySearchData: LiveData<EmptySearchData>
        get() = _emptySearchData
    private val _emptySearchData = SingleLiveEvent<EmptySearchData>()

    internal val searchResults: LiveData<List<Question>>
        get() = _searchResults
    private val _searchResults = SingleLiveEvent<List<Question>>()

    internal val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal var searchPayload: SearchPayload = SearchPayload("")

    internal fun search(searchPayload: SearchPayload = this.searchPayload) {
        this.searchPayload = searchPayload
        launchRequest {
            if (searchPayload.isNotEmpty()) {
                searchDao.insert(
                    SearchEntity(
                        query = searchPayload.query,
                        isAccepted = searchPayload.isAccepted,
                        minNumAnswers = searchPayload.minNumAnswers,
                        bodyContains = searchPayload.bodyContains,
                        isClosed = searchPayload.isClosed,
                        tags = searchPayload.tags?.joinToString(";"),
                        titleContains = searchPayload.titleContains,
                        site = siteStore.site
                    )
                )
                val result = searchService.search(
                    query = searchPayload.query,
                    isAccepted = searchPayload.isAccepted,
                    minNumAnswers = searchPayload.minNumAnswers,
                    bodyContains = searchPayload.bodyContains,
                    isClosed = searchPayload.isClosed,
                    tags = searchPayload.tags?.joinToString(";"),
                    titleContains = searchPayload.titleContains
                ).items
                if (result.isEmpty()) {
                    fetchEmptySearchData()
                } else {
                    _searchResults.value = result
                }
            } else {
                fetchEmptySearchData()
            }
        }
    }

    private suspend fun fetchEmptySearchData() {
        val tags = tagService.getPopularTags().items
        val searches = searchDao.getSearches(siteStore.site)
        _emptySearchData.value = EmptySearchData(
            tags = tags,
            searchHistory = searches.map { it.toSearchPayload() }
        )
    }
}
