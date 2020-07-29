package me.tylerbwong.stack.ui.questions.create

import androidx.hilt.lifecycle.ViewModelInject
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.ui.BaseViewModel

class CreateQuestionViewModel @ViewModelInject constructor(
    private val questionService: QuestionService
) : BaseViewModel() {

    fun createQuestion(title: String, body: String, tags: String, isPreview: Boolean) {
        launchRequest {
            questionService.addQuestion(title, body, tags.split(","), preview = isPreview)
        }
    }
}
