package me.tylerbwong.stack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.data.model.Site
import me.tylerbwong.stack.data.repository.SiteRepository

class DeepLinkingViewModel(private val siteRepository: SiteRepository) : BaseViewModel() {

    internal val site: LiveData<Site>
        get() = _site
    private val _site = MutableLiveData<Site>()

    fun changeSite(site: String) = siteRepository.changeSite(site)

    fun getSite(site: String) {
        viewModelScope.launch {
            _site.value = siteRepository.getSite(site)
        }
    }
}
