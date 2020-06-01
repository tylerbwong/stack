package me.tylerbwong.stack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.repository.SiteRepository

class DeepLinkingViewModelFactory(
    private val siteRepository: SiteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DeepLinkingViewModel(siteRepository) as T
    }
}
