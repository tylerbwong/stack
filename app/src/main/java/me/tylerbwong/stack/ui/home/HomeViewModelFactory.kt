package me.tylerbwong.stack.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.repository.QuestionRepository
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val questionRepository: QuestionRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(questionRepository) as T
    }
}
