package me.tylerbwong.stack.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.repository.SiteRepository
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(
    private val siteRepository: SiteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = SettingsViewModel(siteRepository) as T
}
