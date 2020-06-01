package me.tylerbwong.stack.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.repository.SiteRepository

class SettingsViewModelFactory(
    private val siteRepository: SiteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = SettingsViewModel(siteRepository) as T
}
