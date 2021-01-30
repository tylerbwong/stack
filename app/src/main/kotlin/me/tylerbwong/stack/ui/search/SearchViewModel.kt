package me.tylerbwong.stack.ui.search

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.ACTIVITY
import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.api.service.SearchService
import me.tylerbwong.stack.api.service.TagService
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.entity.SearchEntity
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.data.toSearchPayload
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val tagService: TagService,
    private val searchService: SearchService,
    private val searchDao: SearchDao,
    private val siteStore: SiteStore
) : BaseViewModel() {

    internal val emptySearchData: LiveData<EmptySearchData>
        get() = _emptySearchData
    private val _emptySearchData = SingleLiveEvent<EmptySearchData>()

    internal val searchResults: LiveData<SearchData>
        get() = _searchResults
    private val _searchResults = SingleLiveEvent<SearchData>()

    internal val siteLiveData: LiveData<String>
        get() = siteStore.siteLiveData

    internal var searchPayload = SearchPayload.empty()
    @Sort
    internal var sort = ACTIVITY

    internal fun search(
        searchPayload: SearchPayload = this.searchPayload,
        @Sort sort: String = this.sort
    ) {
        this.searchPayload = searchPayload
        this.sort = sort
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
                    titleContains = searchPayload.titleContains,
                    sort = sort
                ).items
                if (result.isEmpty()) {
                    fetchEmptySearchData(sort)
                } else {
                    _searchResults.value = SearchData(sort, result)
                }
            } else {
                fetchEmptySearchData(sort)
            }
        }
    }

    private suspend fun fetchEmptySearchData(@Sort sort: String) {
        val tags = tagService.getPopularTags().items
        val searches = searchDao.getSearches(siteStore.site)
        _emptySearchData.value = EmptySearchData(
            sort = sort,
            tags = tags.chunked(tags.size / 3),
            searchHistory = searches.map { it.toSearchPayload() }
        )
    }
}
