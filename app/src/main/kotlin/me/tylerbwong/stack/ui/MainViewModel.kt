package me.tylerbwong.stack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.repository.SiteRepository
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val siteRepository: SiteRepository,
) : BaseViewModel() {

    internal val isAuthenticatedLiveData: LiveData<Boolean>
        get() = authRepository.isAuthenticatedLiveData

    internal val siteLiveData: LiveData<String>
        get() = siteRepository.siteLiveData

    internal val currentSite: LiveData<Site>
        get() = _currentSite
    private val _currentSite = MutableLiveData<Site>()

    internal fun fetchSites() {
        launchRequest {
            try {
                siteRepository.fetchSitesIfNecessary(forceUpdate = true)
                _currentSite.value = siteRepository.getCurrentSite()
            } catch (ex: HttpException) {
                Timber.e(ex)
            }
        }
    }
}
