package me.tylerbwong.stack.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.network.service.UserService
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val userService: UserService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(userService) as T
    }
}
