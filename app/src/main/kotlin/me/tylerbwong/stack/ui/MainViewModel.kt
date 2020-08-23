package me.tylerbwong.stack.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.model.User
import retrofit2.HttpException

internal class MainViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val user: LiveData<User?>
        get() = _user
    private val _user = MutableLiveData<User?>()

    internal val isAuthenticated: LiveData<Boolean>
        get() = authRepository.isAuthenticated

    internal fun fetchUser() {
        launchRequest {
            try {
                _user.value = authRepository.getCurrentUser()
            } catch (ex: HttpException) {
                _user.value = null
            }
        }
    }
}
