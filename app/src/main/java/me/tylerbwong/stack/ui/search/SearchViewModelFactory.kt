package me.tylerbwong.stack.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.persistence.dao.SearchDao

class SearchViewModelFactory(
    private val tagService: TagService,
    private val searchService: SearchService,
    private val searchDao: SearchDao,
    private val siteStore: SiteStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(tagService, searchService, searchDao, siteStore) as T
    }
}
