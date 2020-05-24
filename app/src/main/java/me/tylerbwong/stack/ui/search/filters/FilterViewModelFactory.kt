package me.tylerbwong.stack.ui.search.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FilterViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilterViewModel() as T
    }
}
