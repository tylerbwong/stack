package me.tylerbwong.stack.ui.settings.sites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.repository.SiteRepository
import javax.inject.Inject

class SitesViewModelFactory @Inject constructor(
    private val siteRepository: SiteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = SitesViewModel(siteRepository) as T
}
