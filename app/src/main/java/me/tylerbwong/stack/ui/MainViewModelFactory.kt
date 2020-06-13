package me.tylerbwong.stack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.auth.AuthRepository
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(authRepository) as T
    }
}
