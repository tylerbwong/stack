package me.tylerbwong.stack.data.repository

import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.api.service.QuestionService
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val questionService: QuestionService) {

    suspend fun getQuestions(@Sort sort: String): List<Question> {
        return getQuestionsFromNetwork(sort)
    }

    private suspend fun getQuestionsFromNetwork(@Sort sort: String): List<Question> {
        return questionService.getQuestions(sort = sort).items
    }
}
