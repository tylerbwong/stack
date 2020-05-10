package me.tylerbwong.stack.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val tagService: TagService,
    private val searchService: SearchService,
    private val searchDao: SearchDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKE_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(tagService, searchService, searchDao) as T
    }

}
