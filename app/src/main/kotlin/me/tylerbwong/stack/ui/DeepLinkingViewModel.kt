package me.tylerbwong.stack.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.LoginResult
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class DeepLinkingViewModel @Inject constructor(
    private val deepLinker: DeepLinker,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    internal val loginResult: LiveData<LoginResult>
        get() = _loginResult
    private val _loginResult = SingleLiveEvent<LoginResult>()

    fun resolvePath(context: Context, uri: Uri) = deepLinker.resolvePath(context, uri)

    fun logIn(uri: Uri) {
        launchRequest {
            _loginResult.value = authRepository.logIn(uri)
        }
    }
}
