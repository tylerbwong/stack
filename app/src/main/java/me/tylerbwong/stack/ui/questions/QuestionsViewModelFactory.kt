package me.tylerbwong.stack.ui.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tylerbwong.stack.data.network.service.QuestionService
import javax.inject.Inject

class QuestionsViewModelFactory @Inject constructor(
    private val questionService: QuestionService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionsViewModel(questionService) as T
    }
}
