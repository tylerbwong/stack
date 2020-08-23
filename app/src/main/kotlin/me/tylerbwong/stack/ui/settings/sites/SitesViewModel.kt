package me.tylerbwong.stack.ui.settings.sites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.LogOutResult
import me.tylerbwong.stack.data.model.Site
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.data.toSite
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent

class SitesViewModel @ViewModelInject constructor(
    private val siteRepository: SiteRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val logOutState: LiveData<SiteLogOutResult>
        get() = mutableLogOutState
    private val mutableLogOutState = SingleLiveEvent<SiteLogOutResult>()

    internal val sites: LiveData<List<Site>>
        get() = mutableSites
    private val mutableSites = MutableLiveData<List<Site>>()

    internal val isAuthenticated: Boolean
        get() = authRepository.isAuthenticated.value ?: false

    internal var currentQuery: String? = null

    internal fun changeSite(site: String) = siteRepository.changeSite(site)

    internal fun fetchSites() {
        launchRequest { siteRepository.fetchSitesIfNecessary() }
        streamRequest(siteRepository.getSites()) { results ->
            mutableSites.value = results.map { it.toSite() }
        }
    }

    internal fun logOut(siteParameter: String) {
        launchRequest {
            when (authRepository.logOut()) {
                is LogOutResult.LogOutError -> mutableSnackbar.value = Unit
                is LogOutResult.LogOutSuccess -> {
                    mutableSnackbar.value = null
                    mutableLogOutState.value = SiteLogOutResult.SiteLogOutSuccess(siteParameter)
                }
            }
        }
    }
}

sealed class SiteLogOutResult {
    class SiteLogOutSuccess(val siteParameter: String) : SiteLogOutResult()
}
