package me.tylerbwong.stack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.data.auth.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val user: LiveData<User?>
        get() = _user
    private val _user = MutableLiveData<User?>()

    internal val isAuthenticatedLiveData: LiveData<Boolean>
        get() = authRepository.isAuthenticatedLiveData

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
