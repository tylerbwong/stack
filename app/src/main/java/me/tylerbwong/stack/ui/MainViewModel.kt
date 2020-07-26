package me.tylerbwong.stack.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutError
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutSuccess
import retrofit2.HttpException

internal class MainViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val profileImage: LiveData<String?>
        get() = _profileImage
    private val _profileImage = MutableLiveData<String?>()

    internal val isAuthenticated: LiveData<Boolean>
        get() = authRepository.isAuthenticated

    internal fun fetchUser() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                _profileImage.value = user?.profileImage
            } catch (ex: HttpException) {
                _profileImage.value = null
            }
        }
    }

    internal fun logOut() {
        viewModelScope.launch {
            when (authRepository.logOut()) {
                is LogOutError -> mutableSnackbar.value = Unit
                is LogOutSuccess -> {
                    _profileImage.value = null
                    mutableSnackbar.value = null
                }
            }
        }
    }
}
