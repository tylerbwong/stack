package me.tylerbwong.stack.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.LogOutResult
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val siteRepository: SiteRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val user: LiveData<User?>
        get() = _user
    private val _user = MutableLiveData<User?>()

    internal val currentSite: LiveData<Site>
        get() = _currentSite
    private val _currentSite = MutableLiveData<Site>()

    internal val isAuthenticated: LiveData<Boolean>
        get() = authRepository.isAuthenticatedLiveData

    internal fun fetchData() {
        fetchUser()
        fetchCurrentSite()
    }

    internal fun logOut() {
        launchRequest {
            when (authRepository.logOut()) {
                is LogOutResult.LogOutError -> mutableSnackbar.value = Unit
                is LogOutResult.LogOutSuccess -> {
                    _user.value = null
                    mutableSnackbar.value = null
                }
            }
        }
    }

    private fun fetchUser() {
        launchRequest {
            _user.value = authRepository.getCurrentUser()
        }
    }

    private fun fetchCurrentSite() {
        launchRequest {
            siteRepository.fetchSitesIfNecessary()
            _currentSite.value = siteRepository.getCurrentSite()
        }
    }
}
